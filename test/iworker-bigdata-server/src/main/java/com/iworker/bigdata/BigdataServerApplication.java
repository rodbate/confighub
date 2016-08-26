package com.iworker.bigdata;

import com.iworker.bigdata.conf.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DynamicDataSourceRegister.class)
@EnableEurekaClient
public class BigdataServerApplication {
	
	
//	@Bean
//    @LoadBalanced
//    HiveTemplate hiveTemplate() {
//        return new HiveTemplate();
//    }
	
	public static void main(String[] args) {
		SpringApplication.run(BigdataServerApplication.class, args);
	}
}
