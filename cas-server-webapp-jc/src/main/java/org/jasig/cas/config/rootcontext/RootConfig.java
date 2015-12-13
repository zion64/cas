package org.jasig.cas.config.rootcontext;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
    JpaEnvironmentConfig.class,
})
@ComponentScan(basePackages = {"org.jasig.cas"})
public class RootConfig {

}
