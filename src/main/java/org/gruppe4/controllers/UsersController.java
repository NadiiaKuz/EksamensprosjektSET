package org.gruppe4.controllers;

import org.gruppe4.services.UsersService;

public class UsersController {

    private UsersService userService;

    public UsersController(UsersService userService){
        this.userService = userService;
    }



}
