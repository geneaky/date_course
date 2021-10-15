package me.toy.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("spring.datasource.hikari")
public class DatabaseMainProperties {

  private DatabaseConfigurationProperties master;
  private DatabaseConfigurationProperties slave;

  @Getter
  @Setter
  @Component
  public static final class DatabaseConfigurationProperties {

    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
  }
}
