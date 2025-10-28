package org.gruppe4.dto;

import org.gruppe4.enums.UserType;

public class UserDTO {

    private String name;
    private String mail;
    private UserType userType;

    public UserDTO(String name, String mail, UserType userType) {
        this.name = name;
        this.mail = mail;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
