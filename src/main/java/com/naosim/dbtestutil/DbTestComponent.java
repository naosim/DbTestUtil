package com.naosim.dbtestutil;

import com.naosim.dbtestutil.db.DbTestQueryMapper;
import com.naosim.dbtestutil.db.TableInsertEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DbTestComponent {
    private final DbTestQueryMapper mapper;

    public DbTestComponent(@Autowired DbTestQueryMapper mapper) {
        this.mapper = mapper;
    }

    public void exec(String ddl) {
        mapper.exec(ddl);
    }

    public void dropTable(String tableName) {
        mapper.dropTable(tableName);
    }

    public void insert(TableInsertEvent tableInsertEvent) {
        mapper.insert(tableInsertEvent);
    }

    public List<Map<String, Object>> selectAll(String tableName) {
        return mapper.selectAll(tableName);
    }
}
