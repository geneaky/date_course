package me.toy.server.config;

import static me.toy.server.utils.constants.DataSourceType.MASTER;
import static me.toy.server.utils.constants.DataSourceType.SLAVE;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import me.toy.server.config.DatabaseMainProperties.DatabaseConfigurationProperties;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "mainEntityManagerFactory",
    transactionManagerRef = "mainJpaTransactionManager",
    basePackages = {"me.toy.server"}
)
public class TransactionRoutingConfig {

  private final DatabaseMainProperties databaseMainProperties;
  private final Properties jpaCommonPropertiesConfiguration;

  @Bean
  public DataSource masterDataSource() {
    DatabaseConfigurationProperties master = databaseMainProperties.getMaster();
    return DataSourceBuilder.create().type(HikariDataSource.class)
        .driverClassName(master.getDriverClassName())
        .url(master.getJdbcUrl())
        .username(master.getUsername())
        .password(master.getPassword())
        .build();
  }

  @Bean
  public DataSource slaveDataSource() {
    DatabaseConfigurationProperties slave = databaseMainProperties.getSlave();
    return DataSourceBuilder.create().type(HikariDataSource.class)
        .driverClassName(slave.getDriverClassName())
        .url(slave.getJdbcUrl())
        .username(slave.getUsername())
        .password(slave.getPassword())
        .build();
  }

  @Bean
  public DataSource routingDataSource(
      @Qualifier("masterDataSource") DataSource masterDataSource,
      @Qualifier("slaveDataSource") DataSource slaveDataSource) {
    Map<Object, Object> dataSourceMap = new HashMap<>();
    dataSourceMap.put(MASTER, masterDataSource());
    dataSourceMap.put(SLAVE, slaveDataSource());

    return new RoutingDataSource(dataSourceMap);
  }

  @Bean
  @Primary
  public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
    return new LazyConnectionDataSourceProxy(routingDataSource);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean mainEntityManagerFactory(
      @Qualifier("dataSource") DataSource mainDataSource) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
    em.setDataSource(mainDataSource);
    em.setPackagesToScan("me.toy.server");
    em.setJpaProperties(jpaCommonPropertiesConfiguration);

    return em;
  }

  @Bean
  public PlatformTransactionManager mainJpaTransactionManager(@Qualifier("mainEntityManagerFactory")
      EntityManagerFactory mainEntityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(mainEntityManagerFactory);
    return transactionManager;
  }

  @Bean
  public TransactionTemplate transactionTemplate(
      @Qualifier("mainJpaTransactionManager") PlatformTransactionManager transactionManager) {
    return new TransactionTemplate(transactionManager);
  }
}
