package it.aulab.progetto_finale.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.aulab.progetto_finale.services.CustomUserDetailService;

@Configuration
@EnableWebSecurity


public class SecurityConfig {

    @Autowired
    private CustomUserDetailService customUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
http
.csrf(csrf->csrf.disable())
.authorizeHttpRequests((authorize)->
authorize.requestMatchers("/register/**").permitAll()
.requestMatchers("/admin/dashboard","/categories/create", "/categories/edit/{id}", "/categories/update/{id}", "/categories/delete/{id}" ).hasRole("ADMIN")
.requestMatchers("/register/","/","/articles", "/images/**","/articles/detail/**", "/categories/search/{id}", "/search/{id}").permitAll()
.anyRequest().authenticated()
).formLogin(form->
form.loginPage("/login")
.loginProcessingUrl("/login")
.defaultSuccessUrl("/")
.permitAll()
).logout(logout->logout
.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
.permitAll()
).exceptionHandling(exception -> exception.accessDeniedPage("/error/403"))
.sessionManagement(session->session
.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
.maximumSessions(1)
.expiredUrl("/login?session-expired=true")
);
return http.build();

}


@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
}

@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
}
}
