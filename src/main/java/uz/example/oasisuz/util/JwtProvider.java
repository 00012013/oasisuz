package uz.example.oasisuz.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.example.oasisuz.exception.TokenExpiredException;

import java.util.Date;


@Component
public class JwtProvider {

    private static final String secretKey = "oasisSecretKey";
    private static final int expiredTime = 1000 * 60 * 10;// 10 minute
    private static final int expiredTimeForRefresh = 1000 * 60 * 30;// 30 minutes

    public String generateToken(String email){
        return Jwts
                .builder()
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .compact();
    }

    public String generateRefreshToken(String email){
        return Jwts
                .builder()
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeForRefresh))
                .compact();
    }

    public String  getSubject(String token){
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch (ExpiredJwtException e){
            throw new TokenExpiredException(token);
        } catch (Exception e){
            return null;
        }
    }

    public boolean isTokenValid(String token){
        Date expireDate;
        try {
            expireDate = Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
        }catch (ExpiredJwtException e){
            throw new TokenExpiredException(token);
        }catch (Exception e){
            return false;
        }
        return !expireDate.before(new Date());
    }
}
