package com.moviebuzz.users.authentication.config;

import com.moviebuzz.users.filter.JwtRequestFilter;
import com.moviebuzz.users.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // HTTP requests to /authenticate need not be authenticated.
        // all the rest of the requests should be authenticated
        //we want all the requests to be stateless, by default authentication is stateful,
        // state will be maintained by JWT

        httpSecurity.csrf().disable()
            .authorizeRequests()
            .antMatchers("/v1.0/user/authenticate").permitAll()
            .antMatchers(HttpMethod.POST, "/v1.0/user").hasAuthority("ADMIN")
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling().and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    /**
     * Initializing Password encoder is must.
     * Because we don't want to store plane text passwords.
     * @return
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * we need authenticationManager bean to authenticate users in /authenticate API call
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
