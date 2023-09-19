package com.beetech.springsecurity.web.security;

import com.beetech.springsecurity.domain.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final ApplicationContext applicationContext;
    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/",
                                "/api/v1/auth/register",
                                "/api/v1/auth/request-password",
                                "/api/v1/auth/reset-password",
                                "/api/v1/auth/login",
                                "/api/v1/categories",
                                "/api/v1/categories/{category_id}",
                                "/api/v1/products",
                                "/api/v1/products/{productId}",
                                "/api/v1/cart/add-cart",
                                "/api/v1/cart/delete-cart",
                                "/api/v1/city/getAllCity",
                                "/api/v1/district/getAllDistrict",
                                "/api/v1/cart/cart-quantity",
                                "/api/v1/cart",
                                "/api/v1/order/update-order"

                                ).permitAll())

                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/api/v1/admin/user/{id}",
                                "/api/v1/products/create-product",
                                "/api/v1/products/update-product",
                                "/api/v1/products/delete/{productId}",
                                "/api/v1/users/profile",
                                "/api/v1/users/search",
                                "/api/v1/categories/create-category",
                                "/api/v1/categories/update-category",
                                "/api/v1/categories/update-category",
                                "/api/v1/admin/delete-user/{email}"

                                ).hasAuthority("ADMIN"))
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
