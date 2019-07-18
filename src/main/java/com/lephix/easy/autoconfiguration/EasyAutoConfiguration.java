package com.lephix.easy.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EasyProperties.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class EasyAutoConfiguration {

    private ApplicationContext applicationContext;
    private EasyProperties easyProperties;

    EasyAutoConfiguration(ApplicationContext applicationContext, EasyProperties easyProperties) {
        this.applicationContext = applicationContext;
        this.easyProperties = easyProperties;
    }


}
