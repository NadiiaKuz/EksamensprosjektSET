package org.gruppe4.services;

import org.gruppe4.model.User;
import org.gruppe4.repository.UsersRepository;

public class UsersService {

    private UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public User getUserByName(String userName) {
        return usersRepository.getUserByName(userName);
    }
}
