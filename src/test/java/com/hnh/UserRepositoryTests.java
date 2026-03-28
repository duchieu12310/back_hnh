package com.hnh;

import com.hnh.config.security.UserDetailsImpl;
import com.hnh.entity.authentication.Role;
import com.hnh.entity.authentication.User;
import com.hnh.repository.authentication.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetUserByUsername() {
        User user = userRepository.findByUsername("dnucator0").orElseThrow(() -> new RuntimeException("User not found"));
        Set<Role> roles = user.getRoles();
        UserDetailsImpl test = UserDetailsImpl.build(user);
    }

}

