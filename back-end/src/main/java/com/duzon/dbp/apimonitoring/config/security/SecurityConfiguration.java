package com.duzon.dbp.apimonitoring.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

/**
 * SecurityConfiguration
 * 
 * @RequiredArgsConstructor
 * @Configuration
 * [CLASS] WebSecurityConfigurerAdapter
 * [INTERFACE] AuthenticationManager
 * 
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        
        httpSecurity
                    .httpBasic().disable() // 기본설정 사용X
                    .csrf().disable() // csrf 보안 해제
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 해제 (Jwt토큰으로 인증)
                    .and()  // 페이지별 접근 세팅
                        .authorizeRequests()    // 해당 권한에 부여한 리퀘스트 세팅
                            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                            .antMatchers("/auth/**", "/swagger-api-docs-json/*", "/ws/**").permitAll() // signin, signup은 모두에게 허용
                            // .antMatchers(HttpMethod.GET, "/apiMonitor/**").permitAll() // hellowworld은 모두에게 허용
                            .antMatchers("/admin/**").hasRole("ADMIN")   // ADMIN만 유저 목록 가능
                            .anyRequest().hasAnyRole("USER", "ADMIN") // 나머지 page는 해당되는 인증된 회원만 접근
                    .and()
                        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                    .and()
                        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                    .and()
                        // 필터 순서 : (1)jwt token filter (2)id/password filter
                        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class).cors().and();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // - (3)
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    /**
     * 스웨거 페이지 세팅
     */
    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }
}