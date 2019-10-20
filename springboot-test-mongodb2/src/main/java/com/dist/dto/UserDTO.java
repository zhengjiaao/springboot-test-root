package com.dist.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "users")
@Data
public class UserDTO implements Serializable{
    private String id;
    private String username;
    private Integer age;
}