package gr.hephaestus.weatherstation.service.config.security.dto;

/*
 *
 *  @Code from GitHub repository :  https://github.com/xartokoptiko/spring-jwt-conf
 *
 */

public class LoginUserDto {
    private String email;

    private String password;

    public LoginUserDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
