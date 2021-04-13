package com.spring.mongo.api;

import com.spring.mongo.api.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = SpringMongodbApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class SpringMongodbApplicationTests {
	@Autowired
	public TestRestTemplate restTemplate;

	@LocalServerPort
	public int port;

	public String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void testCreateEmployee() {
		Book employee = new Book();
		employee.setDepName("admin");
		employee.setManagerName("admin");
		employee.setId(1);
		ResponseEntity<Book> postResponse = restTemplate.postForEntity(getRootUrl() + "/addManager", employee, Book.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}

	@Test
	public void testUpdateEmployee() {
		int id = 1;
		Book employee = restTemplate.getForObject(getRootUrl() + "/editManager/" + id, Book.class);
		employee.setDepName("admin1");
		employee.setManagerName("admin2");
		restTemplate.put(getRootUrl() + "/employees/" + id, employee);
		Book updatedEmployee = restTemplate.getForObject(getRootUrl() + "/editManager/" + id, Book.class);
		assertNotNull(updatedEmployee);
	}
	@Test
	public void contextLoads1() {
	}

	@Test
	public void testGetEmployeeById() {
		Book employee = restTemplate.getForObject(getRootUrl() + "/find/1", Book.class);
		System.out.println(employee.getDepName());
		assertNotNull(employee);
	}

	@Test
	public void testGetAllEmployees() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/findAll",
				HttpMethod.GET, entity, String.class);
		assertNotNull(response.getBody());
	}

	@Test
	public void testDeleteEmployee() {
		int id = 2;
		Book employee = restTemplate.getForObject(getRootUrl() + "/delete/" + id, Book.class);
		assertNotNull(employee);
		restTemplate.delete(getRootUrl() + "/delete/" + id);
		try {
			employee = restTemplate.getForObject(getRootUrl() + "/delete/" + id, Book.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}

}
