package com.fdmgroup.mockitoshaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fdmgroup.mockitoshaven.game.dungeon.Locatable;
import com.fdmgroup.mockitoshaven.game.dungeon.LocatableDeserializer;

@SpringBootApplication
@EntityScan("com.fdmgroup")
@EnableAspectJAutoProxy
public class MockitosHavenApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockitosHavenApplication.class, args);
	}
	
	@Bean
	public Module dynamoDemoEntityDeserializer() {
	    SimpleModule module = new SimpleModule();
	    module.addDeserializer(Locatable.class, new LocatableDeserializer());
	    return module;
	}

}
