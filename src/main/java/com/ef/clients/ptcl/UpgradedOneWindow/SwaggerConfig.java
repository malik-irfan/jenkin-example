package com.ef.clients.ptcl.UpgradedOneWindow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Irfan on 20-Feb-19.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
 //               .apiInfo(metaInfo());

    }

//    private ApiInfo metaInfo() {
//
//            ApiInfo apiInfo=new ApiInfo
//            (
//                    "Rest Api for PTCL OneWindow Project",
//                    "Method expose for Compalint Registration, customer info, Self servcie Package info and Bill generation",
//                    "1.0",
//                    "Only for internal use of comapany not for Commercial purpose",
//                     new Contact("Zain Iftikhar","http://www.expertflow.com/","zain.iftikhar@expertflow.com")
//                    "EF@2018",
//                    "http://www.expertflow.com/"
//            );
//            return apiInfo;
//        }



}
