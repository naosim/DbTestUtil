package com.naosim.dbtestutil.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface DbTestQueryMapper {
    @org.apache.ibatis.annotations.InsertProvider(type=InsertProvider.class, method="insert")
    void insert(@Param("tableInsertEvent") TableInsertEvent tableInsertEvent);

    public static class InsertProvider extends SQL {
        public String insert(Map<String, TableInsertEvent> params) {
            TableInsertEvent event = params.get("tableInsertEvent");
            INSERT_INTO(event.getTableName());
            for(int i = 0; i < event.getColumnNames().size(); i++) {
                VALUES(event.getColumnNames().get(i), String.format("#{tableInsertEvent.values[%d]}", i));
            }
            return toString();
        }
    }

    @Select("SELECT * FROM ${tableName}")
    List<Map<String, Object>> selectAll(@Param("tableName") String tableName);

    @Select("SELECT * FROM ${tableName}")
    Map<String, Object> selectOne(@Param("tableName") String tableName);

    @Insert("${ddl}")
    void exec(@Param("ddl") String ddl);

    @Insert("DROP TABLE ${tableName}")
    void dropTable(@Param("tableName") String tableName);
}
