package com.qiyi.zuul;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.qiyi.zuul.filter.AuthenticateFilter;
import com.qiyi.zuul.filter.LoginFilter;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@EnableFeignClients("com.qiyi.zuul.remote.feign")
@EnableZuulProxy
public class QiyiZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(QiyiZuulApplication.class, args);

	}


	@Bean
	public CorsFilter corsFilter() {

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		final CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true); // 允许cookies跨域

		config.addAllowedOrigin("*");// 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080 ,以降低安全风险。。

		config.addAllowedHeader("*");// 允许访问的头信息,*表示全部

//		config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了

//		config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等

    	config.addAllowedMethod("HEAD");

        config.addAllowedMethod("GET");// 允许Get的请求方法

        config.addAllowedMethod("PUT");

        config.addAllowedMethod("POST");

        config.addAllowedMethod("DELETE");

        config.addAllowedMethod("PATCH");

		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);

	}
	
//	@Bean
//	public LoginFilter loginFilter(){
//		return new LoginFilter();
//	}
	
	@Bean
	public AuthenticateFilter authenticateFilter() {
		return new AuthenticateFilter();
	}
	
	@Bean 
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        //允许上传的文件最大值
        factory.setMaxFileSize("200MB"); //KB,MB  
        /// 设置总上传数据总大小  
        factory.setMaxRequestSize("200MB");  
        return factory.createMultipartConfig();  
    }
}
