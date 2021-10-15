package me.toy.server.config;

import java.util.Properties;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class JpaCommonPropertiesConfiguration extends Properties {

  private final JpaCommonProperties jpaCommonProperties;

  @PostConstruct
  private void init() {
    this.setProperty("hibernate.hbm2ddl.auto", jpaCommonProperties.getDdlAuto());
    this.setProperty("hibernate.show_sql", jpaCommonProperties.getShowSql());
    this.setProperty("hibernate.format_sql", jpaCommonProperties.getFormatSql());
  }

  @Override
  public synchronized boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public synchronized int hashCode() {
    return super.hashCode();
  }
}
