package com.example.verificota;

import com.example.verificota.cliente.IConsultaSeguroCliente;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = IConsultaSeguroCliente.class)
public class VerificotaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerificotaApplication.class, args);
	}

}
