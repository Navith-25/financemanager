package com.nibm.project.financemanager.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "sqliteEntityManagerFactory",
        transactionManagerRef = "sqliteTransactionManager",
        basePackages = {"com.nibm.project.financemanager.repository.sqlite"}
)
public class SQLiteDbConfig {

    @Primary // <-- Mark SQLite as the primary datasource
    @Bean(name = "sqliteDataSourceProperties")
    @ConfigurationProperties("sqlite.datasource")
    public DataSourceProperties sqliteDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "sqliteDataSource")
    public DataSource sqliteDataSource() {
        return sqliteDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "sqliteEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sqliteEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");

        return builder
                .dataSource(sqliteDataSource())
                .packages("com.nibm.project.financemanager.model.sqlite")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "sqliteTransactionManager")
    public PlatformTransactionManager sqliteTransactionManager(
            @Qualifier("sqliteEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }
}