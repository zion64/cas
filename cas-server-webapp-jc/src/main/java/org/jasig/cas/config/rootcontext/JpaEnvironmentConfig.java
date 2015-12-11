package org.jasig.cas.config.rootcontext;

import java.sql.SQLException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.jasig.cas.config.environment")
@ComponentScan({ "org.jasig.cas.config.environment" })
public class JpaEnvironmentConfig {

    @Bean(name = "envTransactionManager")
    public PlatformTransactionManager envTransactionManager() throws SQLException {
        return new JpaTransactionManager(this.envEntityManagerFactory());
    }

    @Bean(name = "envEntityManagerFactory")
    public EntityManagerFactory envEntityManagerFactory() throws SQLException {
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("org.jasig.cas.config.environment");
        factory.setDataSource(this.envDataSource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "envDataSource")
    public DataSource envDataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.DERBY).build();
    }

    @Bean(name = "envHibernateExceptionTranslator")
    public HibernateExceptionTranslator envHibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

}
