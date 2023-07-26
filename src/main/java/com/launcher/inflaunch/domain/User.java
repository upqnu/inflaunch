package com.launcher.inflaunch.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @JsonIgnore
    private String password;// 회원 비밀번호

    @Transient   // DB 에 반영안함
    @ToString.Exclude
    @JsonIgnore
    private String re_password;  // 비밀번호 확인 입력

    @Column(nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    @JsonIgnore
    private List<Authority> authorities = new ArrayList<>();

    // xxxToMany 의 경우 만들어두면 편리
    public void addAuthority(Authority... authorities){
        Collections.addAll(this.authorities, authorities);
    }

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @Builder.Default
    private List<Course> courseList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @Builder.Default
    private List<ReportReview> reportReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @Builder.Default
    private List<Cart> cartList = new ArrayList<>();
}
