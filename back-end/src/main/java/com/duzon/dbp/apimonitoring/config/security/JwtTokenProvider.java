package com.duzon.dbp.apimonitoring.config.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("spring.jwt.secret")
    private String secretKey;

    private long tokenValidTime = 1000L * 60 * 60 * 3;  //3h

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getUrlEncoder().encodeToString(secretKey.getBytes());
    }

    // Create Json Web Token
    public String createToken(String userPrimaryKey, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(userPrimaryKey);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder().setClaims(claims) // data
                .setIssuedAt(now) // create data of token
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set dead time
                .signWith(SignatureAlgorithm.HS256, secretKey) // set "encoding algorithm", "secret value"
                .compact();
    }

    /**
     * 
     * NOTE ### Authentication ### - 해당 사용자가 본인이 맞는지를 확인하는 절차 - 파라미터(token)를 통해 NOTE
     * ### UserDetails ### - 계정에 대한 vo라고 생각하면됨 NOTE ###
     * UsernamePasswordAuthenticationToken(vo, null, list) ### - 시큐리티에서 제공되는 클래스 -
     * 해당 값들로 저장해서 반환하는 데 사용 NOTE ### userDetails.getAuthorities() ### - 사용자 계정들의 권한
     * 목록 리스트를 반환
     */
    // 인증 정보를 조회
    // import org.springframework.security.core.Authentication;
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPrimaryKey(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 회원 구분 정보를 추출
    public String getUserPrimaryKey(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 요청 값에 대한 해더에서 token 파싱
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

    // 토큰의 유효성 체크
    // 만료일자 체크
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}