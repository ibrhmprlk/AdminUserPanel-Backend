package com.phegondev.adminuserpanel.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service; // Spring Bean olması için eklendi!

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

// @Service notasyonu eklenerek, Spring'in bu sınıfı bir bağımlılık olarak yönetmesi sağlandı.
@Service
public class JWTUtils {

    // Spring Boot'un uygulamanın çalışması için bu alana erişmesi gerekiyor.
    private SecretKey key;

    // 24 saat milisaniye cinsinden
    private static final long EXPIRATION_TIME = 86400000L;

    // YENİ VE DÜZELTİLMİŞ YAPICI METOT: Spring'in Bean oluşturabilmesi için public yapıldı.
    // KEY değeri Spring'in @Value notasyonu ile application.properties'den çekilmelidir.
    public JWTUtils() {
        // Not: Gerçek bir uygulamada bu key dışarıdan alınmalıdır!
        String secreteString =
                "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673965783678548735687R3";

        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    // --- TOKEN OLUŞTURMA METOTLARI ---

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        // claims kullanmıyorsanız bu HashMap'i kaldırabilirsiniz.
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // --- TOKEN PARÇALAMA METOTLARI ---

    public String extractUsername(String token) {
        // Subject, genellikle kullanıcı adını veya e-postayı tutar.
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        // JwtParser ile token'ı doğrular ve payload'ı (Claims) çıkarır.
        return claimsTFunction.apply(Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload());
    }

    // --- TOKEN DOĞRULAMA METOTLARI ---

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Kullanıcı adı eşleşmeli VE token süresi dolmamış olmalıdır.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        // Token'daki bitiş süresini (Expiration) çeker ve şimdiki zamandan önce olup olmadığını kontrol eder.
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}