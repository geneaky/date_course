package me.toy.server.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.toy.server.annotation.LoginUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@RequiredArgsConstructor
@EnableSwagger2
public class SwaggerConfig {

  private final TypeResolver typeResolver;

  private ApiKey apiKey() {
    return new ApiKey("SESSION-COOKIE", "JSESSIONID", "header");
  }

  private SecurityContext securityContext() {

    return SecurityContext.builder().securityReferences(defaultAuth()).build();
  }

  private List<SecurityReference> defaultAuth() {

    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;

    return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
  }

  @Bean
  public Docket api() {

    return new Docket(DocumentationType.SWAGGER_2)
        .securityContexts(Arrays.asList(securityContext()))
        .securitySchemes(Arrays.asList(apiKey()))
        .ignoredParameterTypes(LoginUser.class)
        .alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class),
            typeResolver.resolve(Page.class)))
        .select()
        .apis(RequestHandlerSelectors.basePackage("me.toy.server.controller"))
        .paths(PathSelectors.any())
        .build();
  }

  @Getter
  @Setter
  @ApiModel
  static class Page {

    @ApiModelProperty(value = "페이지 번호(0..N)")
    private String page;
    @ApiModelProperty(value = "페이지 크기")
    private String size;
    @ApiModelProperty(value = "정렬(id|likes,ASC|DESC)")
    private List<String> sort;
  }
}
