package com.naosim.dbtestutil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DbTestMapConverter {

    /**
     * Mapを変換する
     * @param clazz 変換先のクラス。引数なしのコンストラクタを持つこと。値を入れたいフィールド名はカラム名と同じ、かつ、publicにすること
     * @param obj
     * @param <T>
     * @return
     */
    static <T> T convert(Class<T> clazz, Map<String, Object> obj) {
        if(obj == null) {
            return null;
        }
        Supplier<T> factory = () -> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException|IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

        final T result = factory.get();

        obj.forEach((k, v) -> {
            try {
                Field field = clazz.getField(k);
                Class fieldClass = field.getType();
                field.set(result, convert(v, fieldClass));

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return result;
    }

    static <T> List<T> convert(Class<T> clazz, List<Map<String, Object>> list) {
    return list.stream().map(v -> convert(clazz, v)).collect(Collectors.toList());
}

    /**
     * 型変換
     * @param before
     * @param afterClass
     * @return
     */
    public static Object convert(Object before, Class afterClass) {
        Class beforeClass = before.getClass();

        if(beforeClass.equals(afterClass)) {// 変化なし
            return before;
        } else if(Timestamp.class.equals(beforeClass)) {
            if(LocalDateTime.class.equals(afterClass)) {
                return Timestamp.class.cast(before).toLocalDateTime();
            } else if(LocalDate.class.equals(afterClass)) {
                return Timestamp.class.cast(before).toLocalDateTime().toLocalDate();
            }
        } else if(Long.class.equals(beforeClass)) {
            if(LocalDateTime.class.equals(afterClass)) {
                return new Timestamp(Long.class.cast(before)).toLocalDateTime();
            } else if(LocalDate.class.equals(afterClass)) {
                return new Timestamp(Long.class.cast(before)).toLocalDateTime().toLocalDate();
            } else if(BigDecimal.class.equals(afterClass)) {
                return BigDecimal.valueOf(Long.class.cast(before));
            }
        } else if(Double.class.equals(beforeClass)) {
            if(BigDecimal.class.equals(afterClass)) {
                return BigDecimal.valueOf(Double.class.cast(before));
            }
        } else if(Integer.class.equals(beforeClass)) {
            if(BigDecimal.class.equals(afterClass)) {
                return BigDecimal.valueOf(Integer.class.cast(before).longValue());
            }
        }

        throw new RuntimeException(String.format("型変換失敗 %s -> %s", beforeClass.getSimpleName(), afterClass.getSimpleName()));
    }

}
