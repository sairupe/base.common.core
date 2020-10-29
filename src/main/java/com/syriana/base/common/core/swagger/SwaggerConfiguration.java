//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.syriana.base.common.core.swagger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EnableSwagger2
@ComponentScan({"com.syriana"})
public class SwaggerConfiguration implements WebMvcConfigurer, ApplicationContextAware {

    @Autowired
    private ServletContext servletContext;

    public SwaggerConfiguration() {
    }

    @Bean
    public Docket createRestApi(final Swagger2Properties swagger2Properties) {
        Docket docket = (new Docket(DocumentationType.SWAGGER_2)).pathProvider(new RelativePathProvider(this.servletContext) {
            public String getApplicationBasePath() {
                return swagger2Properties.getCustomBasePath();
            }
        }).apiInfo(this.apiInfo(swagger2Properties)).select().apis(RequestHandlerSelectors.basePackage(swagger2Properties.getBasePackage())).paths(PathSelectors.any()).build();
        if (swagger2Properties.isShowHeader()) {
            docket.securitySchemes(this.securitySchemes(swagger2Properties.getHeaders()));
            docket.securityContexts(this.securityContexts(swagger2Properties.getHeaders()));
        }
        return docket;
    }

    private List<ApiKey> securitySchemes(List<String> headers) {
        ArrayList<ApiKey> apiKeys = new ArrayList();
        if (!CollectionUtils.isEmpty(headers)) {
            Iterator var3 = headers.iterator();

            while(var3.hasNext()) {
                String header = (String)var3.next();
                apiKeys.add(new ApiKey(header, header, "header"));
            }
        }

        return apiKeys;
    }

    private List<SecurityContext> securityContexts(List<String> headers) {
        ArrayList<SecurityContext> securityContexts = new ArrayList();
        securityContexts.add(SecurityContext.builder().securityReferences(this.defaultAuth(headers)).build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth(List<String> headers) {
        ArrayList<SecurityReference> securityReferences = new ArrayList();
        if (!CollectionUtils.isEmpty(headers)) {
            Iterator it = headers.iterator();
            while(it.hasNext()) {
                String header = (String)it.next();
                AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
                AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
                securityReferences.add(new SecurityReference(header, authorizationScopes));
            }
        }
        return securityReferences;
    }

    private ApiInfo apiInfo(Swagger2Properties swagger2Properties) {
        return (new ApiInfoBuilder()).title(swagger2Properties.getTitle()).description(swagger2Properties.getDescription()).termsOfServiceUrl(swagger2Properties.getTermsOfServiceUrl()).contact(swagger2Properties.getContact()).version(swagger2Properties.getVersion()).build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
//        beanFactory.removeBeanDefinition("cachingOperationNameGenerator");
//        BeanDefinitionBuilder genericBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MethodNameOperationNameGenerator.class);
//        beanFactory.registerBeanDefinition("methodNameOperationNameGenerator", genericBeanDefinition.getBeanDefinition());
    }
}
