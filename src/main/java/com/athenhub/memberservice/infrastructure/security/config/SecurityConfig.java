package com.athenhub.memberservice.infrastructure.security.config;

import com.athenhub.memberservice.infrastructure.security.SecurityRoleConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final SecurityRoleConverter roleConverter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    JwtAuthenticationConverter conv = new JwtAuthenticationConverter();
    conv.setJwtGrantedAuthoritiesConverter(roleConverter);

    http.csrf(c -> c.disable())
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/profile/**", "/password/**", "/role/**")
                    .hasRole("MASTER_MANAGER")
                    .anyRequest()
                    .permitAll())
        .oauth2Login(c -> c.disable())
        .oauth2ResourceServer(
            c ->
                c.jwt(jwt -> jwt.jwtAuthenticationConverter(conv))
                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

    return http.build();
  }
}
