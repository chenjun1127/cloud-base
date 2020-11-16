package com.example.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.Properties;

/**
 * @Description: @PropertySource只对properties文件可以进行加载，但对于yml或者yaml不能支持。因此增加对yml支持
 * @Author: chenjun
 * @Date: 2020/11/16 11:20
 */
public class YamlPropertySourceFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String sourceName = name != null ? name : resource.getResource().getFilename();
        if (!resource.getResource().exists()) {
            assert sourceName != null;
            return new PropertiesPropertySource(sourceName, new Properties());
        } else {
            assert sourceName != null;
            if (sourceName.endsWith(".yml") || sourceName.endsWith(".yaml")) {
                Properties propertiesFromYaml = loadYml(resource);
                return new PropertiesPropertySource(sourceName, propertiesFromYaml);
            } else {
                return super.createPropertySource(name, resource);
            }
        }
    }

    private Properties loadYml(EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}