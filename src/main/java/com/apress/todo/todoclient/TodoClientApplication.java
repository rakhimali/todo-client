package com.apress.todo.todoclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodoClientApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TodoClientApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}

	private Logger log =
			LoggerFactory.getLogger(TodoClientApplication.class);

	@Bean
	public CommandLineRunner process(ToDoRestClient client) {
		return args -> {
			Iterable<ToDo> toDos = client.findAll();
			assert toDos != null;
			toDos.forEach(toDo -> log.info(toDo.toString()));
			System.out.println("ALL TODOS DISPLAYED");

			ToDo newToDo = client.upsert(
					new ToDo("Drink plenty of Water daily!"));
			assert newToDo != null;
			log.info(newToDo.toString());
			System.out.println("A NEW TODO INSERTED");

			ToDo toDo = client.findById(newToDo.getId());
			log.info(toDo.toString());
			System.out.println("A NEW TODO FOUND");

			ToDo completed = client.setCompleted(newToDo.getId());
			assert completed.getCompleted();
			log.info(completed.toString());
			System.out.println("A NEW TODO COMPLETED");

			client.delete(newToDo.getId());
			assert client.findById(newToDo.getId()) == null;

			System.out.println("A NEW TODO DELETED");
		};
	}

}
