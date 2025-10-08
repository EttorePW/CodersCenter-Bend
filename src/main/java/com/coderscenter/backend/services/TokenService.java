package com.coderscenter.backend.services;

import com.coderscenter.backend.entities.profile.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class TokenService {

    //Diese Werte
    @Value("${jwt.token.secret}")
    private String jwtSecret;

    //Der Prefix beschreibt was für eine Art von token wir verwenden in unserem fall einen Bearer token
    @Value("${jwt.token.prefix}")
    private String jwtPreFix;


    //Claims sind die Informationen, die in einem JWT enthalten sind. Die Claims die schon fix im JWT drinnen sind nennt man auch registered Claims siehe Methode generateToken()
    //Die Custom Claims können selbst gewählt werden je nachdem was für Informationen man im JWT mitgeben möchte
    //In unsrem beispiel wollen wir die Benutzerrolle mitschicken daher fügen wird diese in die Claims map hinzu
    //Man könnte Custom Claims auch komplett auslassen
    public String generateTokenWithClaims(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("Role", user.getRole().toString());
        user.getPermissions().forEach(permission -> claims.put("Permission", permission.getName()));
        return generateToken(claims, user);
    }

    //Hier wird der Token dann ertsellt als String die Claims können auch ausgelassen werden siehe Beschreibung bei Methode generateTokenWithClaims()
    public String generateToken(Map<String, Object> claims, User user){
        return Jwts.builder()
                .claims(claims)
                //Subject = wem wurde Token ausgestellt
                .subject(user.getUsername())
                //Wann wurde ausgestellt
                .issuedAt(new Date(System.currentTimeMillis()))
                //Wann läuft er ab
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *24))
                //Wie wird verschlüsstelt
                .signWith(getSignInKey())
                .compact();
    }


    //Diese Methode liest den Subject Claim aus also den Usernamen
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Holt einen bestimmten Claim aus dem Token
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    //Claims sind die Properties die im JWT gesetzt werden das beinhaltet sowohl die default claims als auch alle eigenen extra Claims
    //Holt aus dem token alle Claims raus
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    //Beschreibt nach welchen Algorithmus verschlüsselt werden soll und welches Secret zum Decodieren verwendet wird
    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Überprüft ob der Token zum User gehört und ob er expired ist.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);


        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiredAt(token).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiredAt(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
