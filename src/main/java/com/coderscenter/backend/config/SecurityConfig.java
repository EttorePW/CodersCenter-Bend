package com.coderscenter.backend.config;

import com.coderscenter.backend.components.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthFilter jwtAuthFilter;


    private final AuthenticationProvider authenticationProvider;

    //Damit ein Request abgearbeitet werden darf muss er durch alle Filter von SpringSecurity. wenn er einmal abgelehnt wird wird er nicht durchgeführt und alle anderen filter ignoriert
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> {})
                //csrf - Cross-Site-Request-Forgery - deshabilitamos CSRF
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/auth/h2/**")
                        .disable()
                )
                //Befor dieser Filter ausgeführt wird soll der JWT-Filter überprüft werden
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                //Brauchen wir damit die H2 dargestellt wird
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                //Hier wird konfiguriert wer auf welchen Pfad zugreiffen darf
                // er arbeitet von oben nach unten ab bedeutet wenn man einmal nicht berechtigt ist fliegt man sofort eaus
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/h2/**").permitAll()
                        .requestMatchers("/auth/swagger-ui.html", "/auth/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/profileImage/**").permitAll()
                        // Email Test Endpoints - for testing Resend API integration
                        .requestMatchers("/api/email/test/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/profileImage/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/profileImage/**").authenticated()

                        .requestMatchers("/api/profile").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/admin/group/**").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/profile").hasAnyRole("ADMIN","VERWALTUNG")
                        .requestMatchers("/api/auth/register").hasAnyRole("ADMIN","REGISTER")//Role kann Registrieren und auch Einzelberechtigung
                        .requestMatchers(HttpMethod.POST,"/api/student/administration").hasAnyRole("ADMIN","VERWALTUNG")
                        .requestMatchers(HttpMethod.POST,"/api/student").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/employee/administration").hasAnyRole("ADMIN","VERWALTUNG")
                        .requestMatchers(HttpMethod.POST,"/api/employee").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/api/schedule/**").authenticated()
                       .requestMatchers("/api/schedule/**").hasAnyRole("ADMIN","VERWALTUNG","SCHEDULE","TRAINER")
                        .requestMatchers("/api/training-optimization/**").hasAnyRole("ADMIN","VERWALTUNG","SCHEDULE")

                        .requestMatchers("/api/absence/**").hasAnyRole("ADMIN","VERWALTUNG","TRAINER","HONORARTRAINER")

                        .requestMatchers("/api/replacements/**").hasAnyRole("ADMIN","VERWALTUNG","TRAINER","HONORARTRAINER")


                        .requestMatchers(HttpMethod.POST, "api/admin/user/{id}/first-password").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/admin/user/**").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/admin/user/**").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/admin/program").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/admin/subject").authenticated()

                        .requestMatchers("/api/admin/permission/**").hasAnyRole("ADMIN","VERWALTUNG","REGISTER")
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN","VERWALTUNG")// Hier ergänzen wir noch mit den Einzelberechtigungen.

                        .requestMatchers(HttpMethod.GET, "/api/contact-book/**").hasAnyRole("ADMIN","VERWALTUNG","STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/contact-book/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/api/contact-book/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/contact-book/**").hasRole("STUDENT")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .build();
    }
//                                .requestMatchers(POST, "api/auth/login").permitAll()
//                        .requestMatchers("api/login").permitAll()
    //authenticated bedeutet es darf jeder der min eingeloggt ist
//                        .requestMatchers(POST, "api/user/profile").authenticated()
    //HasRole und hasAuthority funktionieren im PRinzip beide gleich beide rufen getAuthorities vom UserDetail auf und prüfen ob der Parameter darin vorkommt
//                        .requestMatchers(DELETE, "api/user").hasAuthority("ADMIN")
    //.requestMatchers(DELETE, "api/user").hasRole("ADMIN")
//                        .requestMatchers(PUT, "api/user").hasAnyRole("ADMIN", "MODERATOR")
    //.requestMatchers(PUT, "api/user").hasAnyAuthority("ROLE_ADMIN", "EDIT_USER")





}
