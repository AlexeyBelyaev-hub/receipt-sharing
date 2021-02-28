package com.alexeybelyaev.receiptsharing.security;

import com.alexeybelyaev.receiptsharing.auth.ApplicationUserServiceImpl;
import com.alexeybelyaev.receiptsharing.auth.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.alexeybelyaev.receiptsharing.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserServiceImpl applicationUserService;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     ApplicationUserServiceImpl applicationUserService,
                                     CustomAuthenticationFailureHandler authenticationFailureHandler) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
              //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
               .csrf().disable()
               .authorizeRequests()
               .antMatchers("/","/index","/user/registration","/user/resendRegistrationToken","/receipt","/badUser**","/login",
                       "/boo","/styles/**","/imgs/**","/registrationConfirm**", "/scripts/**").permitAll()
               .antMatchers("/api/**").hasRole(ADMIN.name())
              // .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(PERSON_WRITE.name())
             //  .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(),ADMIN_TRAINEE.name())
               .anyRequest()
               .authenticated()
               .and()
               .formLogin()
               .loginPage("/login").permitAll()
               .defaultSuccessUrl("/",true)
               .failureHandler(authenticationFailureHandler).permitAll()
       .and()
       .rememberMe()
               .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
       .and()
       .logout()
       .logoutUrl("/logout")
               .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET")) // METHOD SHOULD BE POST if CSRF enabled
       .clearAuthentication(true)
       .invalidateHttpSession(true)
       .deleteCookies("JSESSIONID","remember-me")
       .logoutSuccessUrl("/");
    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails adminUser = User.builder()
//                .username("Alexey")
//                .password(passwordEncoder.encode("a"))
//                //.roles(ApplicationUserRole.ADMIN.name()) // ROLE_PERSON
//                .authorities(ADMIN.grantedAuthoritySet()).build();
//        UserDetails adminUserTrainee = User.builder()
//                .username("Trainee")
//                .password(passwordEncoder.encode("trainee"))
//                //.roles(ApplicationUserRole.ADMIN_TRAINEE.name()) // ROLE_PERSON
//                .authorities(ADMIN_TRAINEE.grantedAuthoritySet()).build();
//        UserDetails user = User.builder()
//                .username("User1")
//                .password(passwordEncoder.encode("user1"))
//                //.roles(ApplicationUserRole.USER.name()) // ROLE_PERSON
//                .authorities(USER.grantedAuthoritySet()).build();
//
//        return new InMemoryUserDetailsManager(adminUser,user,adminUserTrainee);
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }
}
