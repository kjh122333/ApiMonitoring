package com.duzon.dbp.apimonitoring.config;

import java.util.List;

import com.google.common.collect.Lists;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfiguration
 * 
 * NOTE
 * 
 * [복습]@Configuration : SpringBean 등록
 * 
 * 1차 미팅 추가 요구사항 : SWAGGER - SWAGGER : REST Api 문서화 - @EnableSwagger2 : 해당
 * 어노테이션으로 문서화 설정 가능 - [type]Docket : 꼬리표, - select() - ApiSelectorBuilder 인스턴스를
 * 리턴함 - 하위 설정으로 swagger 세팅 - RequestHandlerSelectors : 설정할 곳 지정 - PathSelectors
 * : 상세 지정 - useDefaultResponseMessages : 기본적인 error 응답 세팅 - ApiInfo : 해당 문서에대한
 * 정보에대해 명시해주는 CLASS
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    
    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(swaggerInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.duzon.dbp.apimonitoring"))
            // .paths(PathSelectors.ant("/v1/**"))
            .build()
            .securityContexts(Lists.newArrayList(securityContext()))
            .securitySchemes(Lists.newArrayList(apiKey()))
            .useDefaultResponseMessages(false); // 기본 세팅(200,401,403,404) 메시지 표시 해제
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder()
            .title("API Monitoring Service API List")
            .description("API 관리하는 서비스 입니다.")
            .license("React Front")
            .licenseUrl("http://15.165.25.145:9600")
            .version("1")
            .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("X-AUTH-TOKEN", "X-AUTH-TOKEN", "header");
    }

    private SecurityContext securityContext() {
        return springfox.documentation.spi.service.contexts.SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("X-AUTH-TOKEN", authorizationScopes));
    }
}