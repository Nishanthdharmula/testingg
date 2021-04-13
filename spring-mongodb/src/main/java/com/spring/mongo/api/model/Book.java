package com.spring.mongo.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collection="Book")

public class Book {
	@Transient
	public static final String sequenceName="userSequence";

	@Id
	private int id;
	private String depName;
	private String managerName;

}
