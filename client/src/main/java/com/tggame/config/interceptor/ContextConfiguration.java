package com.tggame.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by bruce on 2018/6/23.
 */
@Component
public class ContextConfiguration implements WebMvcConfigurer {



    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        //会话拦截
        registry.addInterceptor(sessionInterceptor)
                .excludePathPatterns("/webjars/**",
                        "/static/**",
                        "/**doc.html",
                        "/swagger-resources**").addPathPatterns("/**");
    }
}
