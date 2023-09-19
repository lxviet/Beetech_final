package com.beetech.springsecurity.web.security;

import com.beetech.springsecurity.domain.entity.MstUser;
import com.beetech.springsecurity.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // JWT token is sent throw Header Authentication
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Remove word Bearer and space to get JWT token
        String token = tokenHeader.substring(7);

        String username = jwtHelper.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            MstUser mstUser = userRepository.findByEmail(username);

            // Authenticate
            if (jwtHelper.validateToken(token, mstUser)) {
                // Set object to Authentication, let Spring know the request is pass
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(mstUser, null, mstUser.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
