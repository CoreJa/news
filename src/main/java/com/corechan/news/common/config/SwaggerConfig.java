package com.corechan.news.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.corechan.news"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("互联网新闻分类")
                .description("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;随着互联网的发展，大量新闻快速涌现，信息严重过载，使用户较难在有限时间得到符合自身需求的信息。采用大数据技术和LDA对新闻进行有效分类是帮助用户快速准确获取信息，改善用户互联网体验的重要方法。"
                        +"<br/>"
                        +"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本作品依托学校的虚拟平台搭建了大数据平台，实现了新闻的有效分类方法。首先我们从互联网上爬取海量新闻数据，然后将获取到的数据在搭建的云平台上使用LDA算法进行海量文本主题建模，同时解决了在聚类算法不能自动确定主题的问题。随后将新闻分类结果进行可视化展现，最后在已经分类的基础上，向用户推荐其感兴趣的新闻内容，利用安卓app和web应用展现。实践表明本作品可在大数据平台上较好地完成新闻分类与个性化推荐任务。")
                .contact(new Contact("陈睿 李童 阳申湘 江桥","http://news.whutosa.com","i@corechan.cn"))
                .version("1.1.3")
                .build();
    }
}
