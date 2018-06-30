package com.naosim.dbtestutil.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableInsertEvent {
    private final String tableName;
    private final List<String> columnNames;
    private final Object[] values;

    TableInsertEvent(String tableName, List<String> columnNames, Object[] values) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.values = values;
    }

    public static TableInsertEvent createFromMap(String tableName, Map<String, Object> map) {
        List<String> columnNames = new ArrayList<>();
        List<Object> valueList = new ArrayList<>();
        map.entrySet().stream().forEach(entry -> {
            columnNames.add(entry.getKey());
            valueList.add(entry.getValue());
        });

        return new TableInsertEvent(
                tableName,
                columnNames,
                valueList.stream()
                        .map(TableInsertEvent::getValue)
                        .collect(Collectors.toList()).toArray(new Object[valueList.size()])
        );
    }

    public static TableInsertEvent createFromKvList(String tableName, List<Object> kvList) {
        if(kvList.size() % 2 != 0) {
            throw new IllegalArgumentException("List size is not even");
        }
        List<String> columnNames = new ArrayList<>();
        List<Object> valueList = new ArrayList<>();
        for(int i = 0; i < kvList.size(); i += 2) {
            columnNames.add((String)kvList.get(i));
            valueList.add(kvList.get(i + 1));
        }

        return new TableInsertEvent(
                tableName,
                columnNames,
                valueList.stream()
                        .map(TableInsertEvent::getValue)
                        .collect(Collectors.toList()).toArray(new Object[valueList.size()])
        );
    }

    /**
     * オブジェクトから値を取得する
     * バリューオブジェクトの場合は中身を取得する
     * @param v
     * @return
     */
    public static Object getValue(Object v) {
        if(primitiveSet.contains(v.getClass())) {
            return v;
        }
        Optional<Method> method = getPublicMethod(v, "getValue");
        if(method.isPresent()) {
            return methodInvoke(v, method.get());
        }

        Optional<Field> field = getPrivateField(v, "value");
        if(field.isPresent()) {
            return fieldValue(v, field.get());

        }
        return v;
    }

    static final Set<Class> primitiveSet = Stream.of(
            String.class,
            Number.class,
            Integer.class,
            Long.class,
            LocalDate.class,
            LocalDateTime.class
    ).collect(Collectors.toSet());

    private static Optional<Method> getPublicMethod(Object obj, String methodName) {
        try {
            return Optional.of(obj.getClass().getMethod(methodName));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    private static Object methodInvoke(Object obj, Method method) {
        try {
            return method.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static Optional<Field> getPrivateField(Object obj, String fieldName) {
        try {
            return Optional.of(obj.getClass().getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    private static Object fieldValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public Object[] getValues() {
        return values;
    }

    public static class Factory {
        private final String tableName;
        private final List<String> columnNames;

        public Factory(String tableName, List<String> columnNames) {
            this.tableName = tableName;
            this.columnNames = columnNames;
        }

        public TableInsertEvent create(Object... values) {
            if(values.length != columnNames.size()) {
                throw new RuntimeException("カラムの数と長さが合いません");
            }
            return new TableInsertEvent(tableName, columnNames, values);
        }

        public TableInsertEvent create(List<Object> values) {
            return this.create(values.stream().toArray());
        }
    }
}
