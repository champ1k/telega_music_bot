package org.alvl.nix.java.telegamusicbot.repository;

import org.alvl.nix.java.telegamusicbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
        User findUserByChatId(Long chatId);
}
