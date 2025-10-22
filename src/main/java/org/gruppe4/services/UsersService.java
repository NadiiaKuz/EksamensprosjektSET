package org.gruppe4.services;

import org.gruppe4.repository.UsersRepository;

public class UsersService {

    private UsersRepository users;

    public UsersService(UsersRepository usersRepository){
        this.users = usersRepository;
    }

}
