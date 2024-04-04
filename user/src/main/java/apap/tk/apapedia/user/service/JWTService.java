package apap.tk.apapedia.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Data
@Service
public class JWTService  {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String getSignature() {
        return secretKey;
    }

    public Claims getPayload(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignature())
                .parseClaimsJws(token)
                .getBody();
    }
}
