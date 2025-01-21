package br.ufrn.imd.cine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.ufrn.imd")
public class SimpleCineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleCineApplication.class, args);
	}

}
