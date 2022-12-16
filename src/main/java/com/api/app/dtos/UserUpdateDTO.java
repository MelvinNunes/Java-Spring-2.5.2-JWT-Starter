package com.api.app.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class UserUpdateDTO {
    @NotNull(message = "firstName cant be null!")
    @Size(min = 2, message = "firstName must have at least 2 letters!")
    private String firstName;
    @NotNull(message = "lastName cant be null!")
    @Size(min = 2, message = "lastName must have at least 2 letters!")
    private String lastName;
    @Email(message = "Invalid email!")
    @NotNull(message = "Email cant be null!")
    private String email;
    @NotNull(message = "Username cant be null!")
    @Size(min = 5, message = "Username must have at least 5 letters!")
    private String username;
    @NotNull(message = "birthDate cant be null!")
    private Date birthDate;
    @NotNull(message = "contact_1 cant be null!")
    @Size(message = "contact_1 must have at least 5 letters", min = 5)
    private String contact_1;
    private String contact_2;
    private String contact_3;
    @NotNull(message = "Address cant be null!")
    private String address;
}
