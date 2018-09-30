package com.stylefeng.guns.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DruidDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();

        WallConfig wallConfig = new WallConfig();
        wallConfig.setMultiStatementAllow(true);

        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig);
        dataSource.getProxyFilters().add(wallFilter);

        return dataSource;
    }

}