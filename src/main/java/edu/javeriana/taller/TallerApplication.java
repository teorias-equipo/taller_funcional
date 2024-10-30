package edu.javeriana.taller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TallerApplication {

	//EJEMPLO USO: http://localhost:8080/reporte/reporteHTML
	//EJEMPLO USO: http://localhost:8080/reporte/departamento?departamento=Meta

	public static void main(String[] args) {
		SpringApplication.run(TallerApplication.class, args);  // Inicia el contexto de Spring Boot
	}
}
