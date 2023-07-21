package com.launcher.inflaunch.config;

import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;

@Service
public class PrincipalDetailService implements UserDetailsService {
    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        pw.println("loadUserByUsername(" + username + ")");

        //DB조회
        User user = userService.findByUsername(username);

        // 해당 username 의 user 가 DB 에 있다면
        // UserDEtails 생성해서 리턴
        if(user != null){
            PrincipalDetails userDetails = new PrincipalDetails(user);
            userDetails.setUserService(userService);
            return userDetails;
        }

        // 해당 username 의 user 가 없다면?
        // UsernameNotFoundException 을 throw 해주어야 한다.

        return null;
    }
}
