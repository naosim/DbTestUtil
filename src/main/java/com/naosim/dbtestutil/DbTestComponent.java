package com.naosim.dbtestutil;

import com.naosim.dbtestutil.db.DbTestQueryMapper;
import com.naosim.dbtestutil.db.TableInsertEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DbTestComponent {
    private final DbTestQueryMapper mapper;

    public DbTestComponent(@Autowired DbTestQueryMapper mapper) {
        this.mapper = mapper;
    }

    public void exec(String ddl) {
        mapper.exec(ddl);
    }
    public void exec(Path ddlPath) {
        if(!Files.exists(ddlPath)) {
            throw new IllegalArgumentException("file not found: " + ddlPath.toString());
        }
        String ddl;
        try {
            ddl = Files.readAllLines(ddlPath).stream().collect(Collectors.joining("\n")).trim();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        mapper.exec(ddl);
    }

    public void exec(File ddlFile) {
        exec(ddlFile.toPath());
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

    public Map<String, Object> selectOne(String tableName) {
        return mapper.selectOne(tableName);
    }
}
