package me.toy.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.jpa.properties.hibernate")
public class JpaCommonProperties {

  private String ddlAuto;
  private String showSql;
  private String formatSql;
}
