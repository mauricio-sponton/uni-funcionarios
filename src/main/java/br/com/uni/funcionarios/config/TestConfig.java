package br.com.uni.funcionarios.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.uni.funcionarios.domain.service.DBService;

@Configuration
public class TestConfig {

	@Autowired
	private DBService service;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String value;

	@Bean
	boolean instanciaDB() {
		if (value.equals("create")) {
			this.service.instanciaDB();
		}
		return false;
	}
}
