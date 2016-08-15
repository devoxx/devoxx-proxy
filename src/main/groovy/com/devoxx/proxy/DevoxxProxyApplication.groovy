package com.devoxx.proxy

import com.devoxx.proxy.service.CfpUpdateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport

import javax.annotation.PostConstruct
import javax.sql.DataSource

@SpringBootApplication
@EnableSpringDataWebSupport
class DevoxxProxyApplication {

	@Autowired CfpUpdateService cfpUpdateService
	@Autowired DataSource dataSource;

	static void main(String[] args) {
		SpringApplication.run DevoxxProxyApplication, args
	}

	@PostConstruct
	void bootstrap() {
		//cfpUpdateService.updateData()
	}
}
