package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Authority;
import com.launcher.inflaunch.domain.AuthorityRepository;
import com.launcher.inflaunch.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    void init(){
        // Authotity 생성
        Authority auth_member = Authority.builder()
                .name("ROLE_MEMBER")
                .build();
        Authority auth_admin = Authority.builder()
                .name("ROLE_ADMIN")
                .build();
        Authority auth_mentor = Authority.builder()
                .name("ROLE_MENTOR")
                .build();
        Authority auth_student = Authority.builder()
                .name("ROLE_STUDENT")
                .build();

        auth_member = authorityRepository.saveAndFlush(auth_member);
        auth_admin = authorityRepository.saveAndFlush(auth_admin);
        auth_mentor = authorityRepository.saveAndFlush(auth_mentor);
        auth_student = authorityRepository.saveAndFlush(auth_student);

        authorityRepository.findAll().forEach(System.out::println);

        // User 생성
        User user1 = User.builder()
                .email("USER1@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .username("member1")
                .phone("01011111111")
                .build();

        User user2 = User.builder()
                .email("USER2@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .username("member2")
                .phone("01011111111")
                .build();

        User admin1 = User.builder()
                .email("ADMIN1@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .username("admin1")
                .phone("01011111111")
                .build();

        User mentor1 = User.builder()
                .email("MENTOR1@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .username("mentor1")
                .phone("01011111111")
                .build();

        User student1 = User.builder()
                .email("STUDENT1@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .username("student1")
                .phone("01011111111")
                .build();

        user1.addAuthority(auth_member);
        admin1.addAuthority(auth_member, auth_admin);
        mentor1.addAuthority(auth_member, auth_mentor);
        student1.addAuthority(auth_member, auth_student);

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        admin1 = userRepository.save(admin1);
        mentor1 = userRepository.save(mentor1);
        student1 = userRepository.save(student1);

        userRepository.findAll().forEach(pw::println);
    }

}