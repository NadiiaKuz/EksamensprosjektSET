package org.gruppe4.controllers;

import io.javalin.http.Context;
import org.gruppe4.dto.UserDTO;
import org.gruppe4.enums.Role;
import org.gruppe4.enums.UserType;
import org.gruppe4.model.User;
import org.gruppe4.services.UsersService;

// Controller bruker UserService som implementerer CRUD-operasjoner

public class UsersController {

    private UsersService userService;

    public UsersController(UsersService userService){
        this.userService = userService;
    }

    public void getUserByEmail(Context context) {
        String email = context.pathParam("email");

        User fetchedUser = userService.getUserByEmail(email);

        // Transformerer til DTO for å hindre sensetive informasjon(pass osv)
        UserDTO userDTO = new UserDTO(fetchedUser.getFullName(), fetchedUser.getMail(), fetchedUser.getUserType());

        if (fetchedUser != null){
            context.json(userDTO);
        }else {
            context.result("Could not find user " + email);
        }
    }

    public void createUser(Context context) {

        String firstName = context.formParam("firstName");
        String lastName = context.formParam("lastName");
        String email = context.formParam("mail");
        String password = context.formParam("password");
        UserType userType = UserType.valueOf(context.formParam("userType"));
        // By default alle brukere fra create-user.vue er USER
        Role role = Role.USER;

        User newUser = new User(firstName, lastName, email, password, userType, role);

        try{
            userService.createUser(newUser);
            context.redirect("/create-user?created=true");
        } catch (Exception e) {
            context.result("Error: " + e.getMessage());
        }
    }
}
