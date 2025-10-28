package org.gruppe4.controllers;

import io.javalin.http.Context;
import org.gruppe4.dto.UserDTO;
import org.gruppe4.model.User;
import org.gruppe4.services.UsersService;

public class UsersController {

    private UsersService userService;

    public UsersController(UsersService userService){
        this.userService = userService;
    }


    public void getUserByName(Context context) {
        String userName = context.pathParam("name");

        User fetchedUser = userService.getUserByName(userName);

        // Transformerer til DTO for å hindre sensetive informasjon(pass osv)
        UserDTO userDTO = new UserDTO(fetchedUser.getName(), fetchedUser.getMail(), fetchedUser.getUserType());

        if (fetchedUser != null){
            context.json(userDTO);
        }else {
            context.result("Could not find user " + userName);
        }
    }

    public void createUser(Context context) {

    }
}
