package com.api.app.dtos;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserLoginDTO {
    @NotNull(message = "Username cant be null")
    private String username;
    @NotNull(message = "Password cant be null")
    @Size(message = "Password must have at least 5 letters!", min = 5)
    private String password;

}
