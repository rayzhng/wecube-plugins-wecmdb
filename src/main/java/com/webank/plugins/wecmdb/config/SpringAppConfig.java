package com.webank.plugins.wecmdb.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.webank.plugins.wecmdb.ApplicationProperties;
import com.webank.plugins.wecmdb.HttpClientProperties;

@Configuration
@EnableConfigurationProperties({ ApplicationProperties.class, HttpClientProperties.class })
@ComponentScan({ "com.webank.plugins.wecmdb.service" })
public class SpringAppConfig {

}
