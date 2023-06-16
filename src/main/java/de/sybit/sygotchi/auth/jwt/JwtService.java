package de.sybit.sygotchi.auth.jwt;

import de.sybit.sygotchi.auth.controller.response.JwtResponse;
import de.sybit.sygotchi.user.TamagotchiUser;
import de.sybit.sygotchi.user.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    /**
     * JWT Secret Key from application.properties
     * <p> This key is used to sign the JWT Token </p>
     */
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /**
     * JWT Token Expiration in ms from application.properties
     * <p> This value is used to set the expiration date of the JWT Token </p>
     */
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJwtToken(String username) {
        return this.generateJwtToken((UserDetailsImpl) this.userDetailsService.loadUserByUsername(username));
    }

    public JwtResponse buildJwtResponse(String jwtToken, TamagotchiUser user) {
        return new JwtResponse(
                jwtToken,
                user.getId(),
                user.getUsername()
        );
    }


    /**
     * Get User Name from JWT Token
     *
     * @param token JWT Token
     * @return User Name
     */
    public String getUserNameFromJwtToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public UserDetailsImpl getUserDetailsFromJwtToken(String token) {
        Claims claims = extractAllClaims(token);
        return new UserDetailsImpl(
                (String) claims.get("id"),
                claims.getSubject(),
                null
        );
    }

    /**
     * Validate JWT Token and check if it is expired or not yet valid or invalid signature
     *
     * @param authToken JWT Token
     * @return True if valid, false if not
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
