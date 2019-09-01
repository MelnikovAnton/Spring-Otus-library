package ru.otus.library.secutity;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import ru.otus.library.secutity.services.CustomDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final CustomDetailsService customDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .and()
        .csrf().disable();

//        http
//                .antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/favicon.ico","/js/**", "/error**").permitAll()
//                .and().logout().logoutSuccessUrl("/").permitAll()
//                .and().formLogin().loginPage("/login").successForwardUrl("/")
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login","/favicon.ico","/");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RemoteTokenServices remoteTokenServices() {
        final RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        tokenServices.setClientId("client");
        tokenServices.setClientSecret("secret");
        return tokenServices;
    }

//    @Bean
//    public PrincipalExtractor principalExtractor() {
//        return map -> {
//            System.out.println("===========================================");
//            map.forEach((k,v)-> System.out.println(k+ " : " + v));
//
//            return customDetailsService.loadUserByUsername("admin");
//        };
//    }
}
