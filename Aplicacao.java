package br.com.cafeeiradoisirmaos.sistema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "br.com.cafeeiradoisirmaos.sistema.model")
@EnableJpaRepositories(basePackages = "br.com.cafeeiradoisirmaos.sistema.repository")
public class Aplicacao {

    public static void main(String[] args) {
        SpringApplication.run(Aplicacao.class, args);
    }
}
