package com.launcher.inflaunch.repository;


import com.launcher.inflaunch.domain.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 email 으로 조회
    User findByEmail(String email);

    // 사용자 username 으로 조회
    User findByUsername(String username);

    // 회원 탈퇴
    void deleteById(Id id);

}