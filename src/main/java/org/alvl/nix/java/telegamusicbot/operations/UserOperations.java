package org.alvl.nix.java.telegamusicbot.operations;

import org.alvl.nix.java.telegamusicbot.exceptions.UserNotFoundException;
import org.alvl.nix.java.telegamusicbot.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserOperations {
    User findUserById(Integer id) throws UserNotFoundException;

    List<User> findAll();

    void save(User user);

    void delete(Integer id);
}
