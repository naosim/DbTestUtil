package com.naosim.dbtestutil;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.naosim.dbtestutil")
@MapperScan("com.naosim.dbtestutil.db")
public class DbTestScanConfiguration {
}
