package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// other http security config
//		http.cors().configurationSource(corsConfigurationSource());
//	}
//
////This can be customized as required
//CorsConfigurationSource corsConfigurationSource() {
//    CorsConfiguration configuration = new CorsConfiguration();
//    List<String> allowOrigins = Arrays.asList("*");
//    configuration.setAllowedOrigins(allowOrigins);
//    configuration.setAllowedMethods(singletonList("*"));
//    configuration.setAllowedHeaders(singletonList("*"));
//    //in case authentication is enabled this flag MUST be set, otherwise CORS requests will fail
//    configuration.setAllowCredentials(true);
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", configuration);
//    return source;
//}
