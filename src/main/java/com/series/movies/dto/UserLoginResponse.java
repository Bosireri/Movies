package com.series.movies.dto;

class LocalUserDetails{
    private String userName;
    private String email;
    private Integer id;

    public LocalUserDetails(String userName, String email, String s, Integer id) {
        this.userName = userName;
        this.email = email;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

public class UserLoginResponse {
    
    private String token;
    private String message;
    private LocalUserDetails user;
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalUserDetails getUser() {
        return user;
    }

    public void setUser(LocalUserDetails user) {
        this.user = user;
    }

    public void setUser(String userName, String email, Integer id) {
        this.user = new LocalUserDetails(userName,userName, email, id);
    }
}
