package com.fdmgroup;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.context.ServletContextAware;

@SpringBootApplication
@ComponentScan(basePackages = "com.fdmgroup.zorkclone.webcontrollers")
public class Application  {


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}