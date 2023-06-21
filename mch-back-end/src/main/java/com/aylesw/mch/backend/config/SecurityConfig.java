package com.aylesw.mch.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests((authorize) ->
						//authorize.anyRequest().authenticated()
						authorize
								.requestMatchers("/api/v1/auth/**").permitAll()
//								.requestMatchers("/swagger-ui/**").permitAll()
//								.requestMatchers("/v3/api-docs/**").permitAll()
								.anyRequest().authenticated()

//				).exceptionHandling( exception -> exception
//						.authenticationEntryPoint(authenticationEntryPoint)
				).sessionManagement( session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
