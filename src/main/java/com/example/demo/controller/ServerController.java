package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
@RestControllerEndpoint(id = "customEndpoint")
public class ServerController {
	
	private List<String> listado = new ArrayList<>();
	
	private Counter counter;
	
	public ServerController(MeterRegistry registry) {
		this.counter = Counter.builder("invocaciones.cambio").description("Invocaciones totales").register(registry);
	}
	
	@ReadOperation
	public List<String> estados(){
		return listado;
	}
	
	@WriteOperation
	public void writeOperation(@Selector String newStatus) {
		listado.add(newStatus);
	}
	
	@DeleteOperation
	public void deleteOperation(@Selector String deleteStatus) {
		listado.remove(deleteStatus);
	}
	
	@GetMapping(path="/getStatus")
	public String getStatus() {
		if(listado.size()-1 < 0) {
			listado.add("Inicial");
		}
		return listado.get(listado.size()-1);
	}
	
	@GetMapping(path="/cambio")
	public List<String> cambioStatus() {
		listado.add("status");
		counter.increment();
		return listado;
	}
	
	@GetMapping(path="/otroCambio")
	public List<String> otroCambioStatus() {
		listado.add("Otro status");
		return listado;
	}

}
