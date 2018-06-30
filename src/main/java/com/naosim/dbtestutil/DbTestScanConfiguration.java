package com.naosim.dbtestutil;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.naosim.dbtestutil", "com.naosim.dbtestutil.db"})
public class DbTestScanConfiguration {
}
