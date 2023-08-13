/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.ksyun.trade.bootstrap;

import org.apache.ibatis.io.VFS;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class MybatisConfig {
    private String basePackage = "com.ksyun.trade.dao";
    private String typeAliasesPackage = "com.ksyun.trade.dao.dataobject";
    private String mapperLocations = "classpath:mybatis/*.xml";
    private String configLocation = "mybatis.xml";

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 设置config
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(configLocation));
        // 设置mybatis的主配置文件
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocations);
        sqlSessionFactoryBean.setMapperLocations(resources);
        // 设置别名包
        VFS.addImplClass(SpringBootVFS.class);
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        return sqlSessionFactoryBean;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(basePackage);
        return mapperScannerConfigurer;
    }
}
