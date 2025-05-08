package project.psa.dataserver.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.psa.dataserver.repository.NhanvienRepository;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class jwtProvider {
    @Autowired
    NhanvienRepository nhanvienRepository;
    private String jwtSecret ="VUVNDUC050120011234567890QWERTYUIPASDFGHJKLZXCVBNMQASXCFVGBJHBFHCDGVCGNVCGVHCFHBBCAOKFLWJDJGKDJRVJFHJFHDJRHFHDSHRYCHDJSJFHDH"; // Base64.getEncoder().encodeToString(key.getEncoded());
    public static final Long time_expire = 28800000L;

    public String generateToken(String userName) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        Date date = new Date(System.currentTimeMillis() + jwtProvider.time_expire);
        String jwt = Jwts.builder().setSubject(userName).setExpiration(date)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return jwt;
    }

    public boolean validateToken(String token) {
        try {
//            if(accountRepository.findUserByUsername(getLoginFormToke(token)).getStatus() == constant.STATUS.DE_ACTIVE) {
//                return false;
//            }
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoginFormToken(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
