package com.launcher.inflaunch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    PrintWriter pw = new PrintWriter(System.out);

    @Bean
    public PasswordEncoder encoder(){
        pw.println("PasswordEncoder bean 생성");
        return new BCryptPasswordEncoder();
    }

    // Security 동작 x
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return web -> web.ignoring().anyRequest();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화

                /**********************************************
                 * ① request URL 에 대한 접근 권한 세팅  : authorizeHttpRequests()
                 * .authorizeHttpRequests( AuthorizationManagerRequestMatcherRegistry)
                 **********************************************/
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/carts",
                                "/payments",
                                "/suggestion/cart"
                        ).authenticated()
                        .requestMatchers(
                                "/mentor/income",
                                "/mentor/course",
                                "/course/write",
                                "/course/update",
                                "/course/*/video/*"
                        ).hasAnyAuthority("ROLE_MENTOR")
                        .requestMatchers(
                                "/admin/categori",
                                "/admin/income",
                                "/admin/income/detail"
                        ).hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(
                                "/student/courses",
                                "/suggestion/sequence",
                                "/course/*/video/*",
                                "/course/*/review/create"
                        ).hasAnyAuthority("ROLE_STUDENT")
                        .anyRequest().permitAll()
                )

                /********************************************
                 * ② 폼 로그인 설정
                 * .formLogin(HttpSecurityFormLoginConfigurer)
                 *  form 기반 인증 페이지 활성화.
                 *  만약 .loginPage(url) 가 세팅되어 있지 않으면 '디폴트 로그인' form 페이지가 활성화 된다
                 ********************************************/
                .formLogin(form -> form
                        .loginPage("/user/login")// 로그인 필요한 상황 발생시 매개변수의 url (로그인 폼) 으로 request 발생
                        .loginProcessingUrl("/user/login")// "/user/login" url 로 POST request 가 들어오면 시큐리티가 낚아채서 처리, 대신 로그인을 진행해준다(인증).
                        // 이와 같이 하면 Controller 에서 /user/login (POST) 를 굳이 만들지 않아도 된다!
                        // 위 요청이 오면 자동으로 UserDetailsService 타입 빈객체의 loadUserByUsername() 가 실행되어 인증여부 확인진행 <- 이를 제공해주어야 한다.
                        .defaultSuccessUrl("/")// '직접 /login' → /loginOk 에서 성공하면 "/" 로 이동시키기
                        // 만약 다른 특정페이지에 진입하려다 로그인 하여 성공하면 해당 페이지로 이동 (너무 편리!)

                        // 로그인 성공직후 수행할코드
                        .successHandler(new CustomLoginSuccessHandler("/main"))

                        // 로그인 실패하면 수행할 코드
                        .failureHandler(new CustomLoginFailureHandler())
                )

                /********************************************
                 * ③ 로그아웃 설정
                 * .logout(LogoutConfigurer)
                 ********************************************/
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/user/logout")
                        .invalidateHttpSession(false) // Session 삭제 않하기
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                )

                /********************************************
                 * ④ 예외처리 설정
                 * .exceptionHandling(ExceptionHandlingConfigure)
                 ********************************************/
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        // 권한(Authorization) 오류 발생시 수행할 코드
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .build();
    }

}
