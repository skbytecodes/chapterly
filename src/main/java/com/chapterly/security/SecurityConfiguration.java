package com.chapterly.security;

import com.chapterly.util.Oauth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;


    @Autowired
    private Oauth2SuccessHandler oauthSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/register", "/api/v1/authenticate", "/api/v1/open")
                .permitAll()
                .requestMatchers("/api/v1/categories", "/api/v1/search/{key}", "/api/v1/banners"
                        , "/api/v1/featuredAuthors/{count}", "/api/v1/books/category/{categoryName}"
                        , "/api/v1/search/books/{key}", "/api/v1/book/rating/{bookTitle}"
                        , "/api/v1/bookByName/{title}","/api/v1/book/{id}","/api/v1/author/{id}"
                        , "/api/v1/reviews/book/{bookName}","/api/v1/authors/authorByName/{authorName}"
                        , "/api/v1/cart/**","/api/v1/letsTalk")
                .permitAll()
                .requestMatchers("/api/v1/secured").hasRole(Role.ADMIN.name())
                .requestMatchers("/api/v1/order/createOrder", "/api/v1/payment/success").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .oauth2Login(oauth2 -> {
//                    oauth2.successHandler(oauthSuccessHandler);
//                })
//                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000/"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
    }

}
