package unlp.info.bd2.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/bd2_tours?createDatabaseIfNotExist=true&useSSL=false&useTimezone=true&serverTimezone=UTC}")
    private String dbUrl;

    @Value("${spring.datasource.username:root}")
    private String dbUsername;

    @Value("${spring.datasource.password:KeliseiVentura1+}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String dbDriverClassName;

    @Value("${spring.jpa.hibernate.ddl-auto:create}")
    private String ddlAuto;

    @Bean
    @Primary
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(this.dataSource());
        sessionFactory.setPackagesToScan(new String[] { "unlp.info.bd2.model" });
        sessionFactory.setHibernateProperties(this.hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        System.out.println("=== DATABASE CONFIG ===");
        System.out.println("URL: " + dbUrl);
        System.out.println("Username: " + dbUsername);
        System.out.println("Password is empty? " + (dbPassword == null || dbPassword.isEmpty()));

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(dbDriverClassName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        // FIXED: Use injected properties instead of hardcoded values
        dataSource.setDriverClassName(dbDriverClassName);

        // Option A: Use the injected URL directly
        dataSource.setUrl(dbUrl);

        // OR Option B: If you need group-specific databases, uncomment this:
        // String url = dbUrl + "_" + this.getGroupNumber() +
        // "?createDatabaseIfNotExist=true&useSSL=false&useTimezone=true&serverTimezone=UTC";
        // dataSource.setUrl(url);

        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        // Add connection pool settings (optional but recommended)
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(10);
        dataSource.setMinIdle(2);

        return dataSource;
    }

    @Bean
    @Primary
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(this.sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.format_sql", "true");
        hibernateProperties.setProperty("hibernate.use_sql_comments", "false");

        hibernateProperties.setProperty("hibernate.connection.autocommit", "false");
        return hibernateProperties;
    }

    private int getGroupNumber() {
        return 0; // Replace with your actual group number (e.g., 1, 2, 3, etc.)
    }
}