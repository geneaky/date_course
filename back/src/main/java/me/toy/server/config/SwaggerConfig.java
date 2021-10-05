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
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
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

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Open Course REST Project API Doc")
        .version("v.1")
        .description("Open Course REST 프로젝트 API 문서")
        .termsOfServiceUrl("not yet")
        .build();
  }

  @Bean
  public Docket api() {

    return new Docket(DocumentationType.SWAGGER_2)
        .securitySchemes(Arrays.asList(apiKey()))
        .ignoredParameterTypes(LoginUser.class)
        .alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class),
            typeResolver.resolve(Page.class)))
        .apiInfo(apiInfo())
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
    @ApiModelProperty(value = "정렬(컬럼명,ASC|DESC)")
    private List<String> sort;
  }
}
