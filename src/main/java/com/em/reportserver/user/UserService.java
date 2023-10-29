package com.em.reportserver.user;

import java.util.List;

public interface UserService {


    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(long id, User user);

    void deleteUser(long id);

    User getUserById(long id);

    User getUserByUsername(String username);
}
