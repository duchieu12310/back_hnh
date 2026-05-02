package com.hnh.config.security;

import com.hnh.constant.SecurityConstants;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
//        securedEnabled = true,
//        jsr250Enabled = true,
        prePostEnabled = true)
@Order(1000)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final ApplicationContext context;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, ApplicationContext context) {
        this.userDetailsService = userDetailsService;
        this.context = context;
    }

    @Bean
    public AuthTokenFilter authenticationJwAuthTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            HandlerExceptionResolver resolver = context.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
            resolver.resolveException(request, response, null, exception);
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, exception) -> {
            HandlerExceptionResolver resolver = context.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
            resolver.resolveException(request, response, null, exception);
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(SecurityConstants.IGNORING_API_PATHS)
                .permitAll()
                .antMatchers(SecurityConstants.PUBLIC_API_PATHS)
                .permitAll()
                .antMatchers(SecurityConstants.STAFF_SHARED_API_PATHS)
                .hasAnyAuthority(SecurityConstants.Role.ADMIN, SecurityConstants.Role.MANAGER, SecurityConstants.Role.OPERATOR, SecurityConstants.Role.SHIPPER)
                .antMatchers(SecurityConstants.ADMIN_API_PATHS)
                .hasAuthority(SecurityConstants.Role.ADMIN)
                .antMatchers(SecurityConstants.MANAGER_API_PATHS)
                .hasAnyAuthority(SecurityConstants.Role.ADMIN, SecurityConstants.Role.MANAGER)
                .antMatchers(SecurityConstants.OPERATOR_API_PATHS)
                .hasAnyAuthority(SecurityConstants.Role.ADMIN, SecurityConstants.Role.OPERATOR)
                .antMatchers(SecurityConstants.SHIPPER_API_PATHS)
                .hasAnyAuthority(SecurityConstants.Role.ADMIN, SecurityConstants.Role.SHIPPER)
                .antMatchers(SecurityConstants.CLIENT_API_PATHS)
                .hasAuthority(SecurityConstants.Role.CUSTOMER)
                .anyRequest()
                .authenticated();

        http.addFilterBefore(authenticationJwAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}

