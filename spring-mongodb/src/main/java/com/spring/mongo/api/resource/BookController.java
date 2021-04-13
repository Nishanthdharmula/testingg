package com.spring.mongo.api.resource;

import java.util.List;
import java.util.Optional;

import com.spring.mongo.api.exception.ResourceNotFoundException;
import com.spring.mongo.api.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.spring.mongo.api.model.Book;
import com.spring.mongo.api.repository.BookRepository;
import org.springframework.web.client.ResourceAccessException;


@RestController
@RequestMapping("/api")
public class BookController {

	@Autowired
	private BookRepository repository;

	@Autowired
	private SequenceGeneratorService service;

	@Autowired
	MongoOperations mongo;
	
	@PostMapping("/addManager")
	public String saveBook(@RequestBody Book book) {
		book.setId(service.getSequenceNum(Book.sequenceName));
		repository.insert(book);
		return "Added manager with id "+ book.getId();
	}
	@GetMapping("/findAll")
	public List<Book> getBooks(){
		return repository.findAll();
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Book> getBook(@PathVariable int id)throws ResourceNotFoundException {
		Book book=repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Manager Not Found with id: "+id));
		return ResponseEntity.ok().body(book);
	}

	@PutMapping("/editManager/{id}")
	public ResponseEntity<Book> updateManager(@PathVariable int id,
												   @Validated @RequestBody Book employeeDetails) throws ResourceNotFoundException {
		Book employee = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

		employee.setDepName(employeeDetails.getDepName());
		employee.setManagerName(employeeDetails.getManagerName());
		final Book updatedEmployee = repository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}


	/*@PutMapping("/editManager")
	public String editBook(@RequestBody Book book) {
		Book updated= repository.save(book);
		return "Edited manager with id "+ book.getId()+ updated;
	}*/

	@DeleteMapping("/delete/{id}")
	public String deleteBook(@PathVariable int id)
			throws ResourceAccessException, ResourceNotFoundException {
		Book employee = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

		repository.delete(employee);
		return "Deleted with id: "+id;
	}
}
