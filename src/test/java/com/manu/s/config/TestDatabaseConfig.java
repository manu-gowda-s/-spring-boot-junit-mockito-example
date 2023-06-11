package com.manu.s.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@TestConfiguration
public class TestDatabaseConfig
{
    @Primary
    @Bean
    public DataSource dataSource() {
        // Create an in-memory H2 database for testing
        return new org.springframework.jdbc.datasource.embedded.
                EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(org.springframework.jdbc.datasource.
                        embedded.EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("schema.sql")
                .build();
    }
}
