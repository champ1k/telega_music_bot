package org.alvl.nix.java.telegamusicbot.services;
import org.alvl.nix.java.telegamusicbot.exceptions.UserNotFoundException;
import org.alvl.nix.java.telegamusicbot.model.User;
import org.alvl.nix.java.telegamusicbot.operations.UserOperations;
import org.alvl.nix.java.telegamusicbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserOperations {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Integer id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
