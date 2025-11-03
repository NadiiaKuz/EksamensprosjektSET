package org.gruppe4.services;

import org.gruppe4.model.User;
import org.gruppe4.repository.UsersRepository;

public class UsersService {

    private UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public User getUserByEmail(String userEmail) {
        return usersRepository.getUserByEmail(userEmail);
    }

    public void createUser(User newUser) {
        User databaseUser = usersRepository.getUserByEmail(newUser.getMail());

        if (databaseUser != null){
            throw new RuntimeException("User exists with email " + newUser.getMail());
        }

        usersRepository.createUser(newUser);
    }
}
