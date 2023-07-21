package com.launcher.inflaunch.service;


import com.launcher.inflaunch.domain.Authority;
import com.launcher.inflaunch.domain.AuthorityRepository;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public UserService() {
        pw.println(getClass().getName() + "() 생성");
    }

    // username(회원 아이디) 의 User 정보 읽어오기
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    // 특정 username(회원 아이디) 의 회원이 존재하는지 확인
    public boolean isExist(String username){
        User user = findByUsername(username);
        return (user != null) ? true : false;
    }

    // 신규 회원 등록
    public int register(User user){
        user.setUsername(user.getUsername().toUpperCase());   // DB 에는 회원아이디(username) 을 대문자로 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // password 는 암호화 해서 저장해야 한다.
        user = userRepository.save(user);    // 새로 회원(User) 저장.  id 값 받아온다.

        // 신규회원은 ROLE_MEMBER 권한 기본적으로 부여하기
        Authority auth = authorityRepository.findByName("ROLE_MEMBER");
        user.addAuthority(auth);
        userRepository.save(user);

        return 1;
    }

    // 특정 사용자(id)의 authority(들)
    public List<Authority> selectAuthoritiesById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null)
            return user.getAuthorities();

        return new ArrayList<>();
    }

}













