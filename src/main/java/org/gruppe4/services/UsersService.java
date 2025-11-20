package org.gruppe4.services;

import org.gruppe4.model.User;
import org.gruppe4.repository.UsersRepository;

// Implementerer CRUD-operasjoner for User.
// Ansvarlig for businesslogikken mellom controller og repository

public class UsersService {

    private UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    // READ
    public User getUserByEmail(String userEmail) {
        return usersRepository.getUserByEmail(userEmail);
    }

    // CREATE
    public void createUser(User newUser) {
        User databaseUser = usersRepository.getUserByEmail(newUser.getMail());

        if (databaseUser != null){
            throw new RuntimeException("User exists with email " + newUser.getMail());
        }

        // Videre arbeid: hash passord før lagring til database

        usersRepository.createUser(newUser);
    }

    // UPDATE videre arbeid
    // DELETE videre arbeid
}
