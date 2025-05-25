package co.edu.uniquindio.cityguardian.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
@Component
public class JWTUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(String id, Map<String, String> claims) {


        Instant now = Instant.now();


        return Jwts.builder()
                .claims(claims)
                .subject(id)
                .issuedAt(Date.from(now))
                .signWith( getKey() )
                .compact();
    }


    public Jws<Claims> parseJwt(String jwtString) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {
        JwtParser jwtParser = Jwts.parser().verifyWith( getKey() ).build();
        return jwtParser.parseSignedClaims(jwtString);
    }


    private SecretKey getKey(){
        byte[] secretKeyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }


}
