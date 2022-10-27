package app.security;

import app.security.user.QuizUserDetails;
import app.security.user.QuizUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class QuizSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    QuizUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/api/quizzes/**").hasAnyRole("USER", "ADMIN")
                .mvcMatchers(HttpMethod.POST, "/api/register").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/api/user/delete").hasAnyRole("USER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/user/update").hasAnyRole("USER", "ADMIN")
                .mvcMatchers("/actuator/shutdown").permitAll()
                .mvcMatchers("/play").hasAnyRole("USER", "ADMIN")
                .mvcMatchers("/contribute").hasAnyRole("USER", "ADMIN")
                .mvcMatchers("/stats").hasAnyRole("USER", "ADMIN")
                .mvcMatchers("/home").permitAll()
                .mvcMatchers("/register").permitAll()
                .mvcMatchers("/profile").permitAll()
                .mvcMatchers("/").permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .and()
                    .csrf()
                        .disable()
                    .headers()
                        .frameOptions().disable()
                .and()
                    .httpBasic()
                .and()
                    .formLogin(form -> {
                        form
                            .loginPage("/login")
                            .permitAll();
                    });

        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder());
//            .and()
//                .inMemoryAuthentication()
//                    .withUser("admin")
//                        .password(getEncoder().encode("admin"))
//                        .roles("ADMIN")
//                    .and()
//                        .passwordEncoder(getEncoder());
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}

