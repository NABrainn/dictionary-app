package lule.dictionary.service.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.*;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.service.jwt.dto.TokenPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class JwtService {

    @Value("${spring.security.jwt.secret}")
    private String secret;

    private RSAKey privateKey;
    private RSAKey publicKey;

    public JwtService() {
        try {
            privateKey =  new RSAKeyGenerator(2048)
                    .keyID(secret)
                    .generate();
            publicKey = privateKey.toPublicJWK();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public TokenPair generateTokenPair(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        return new TokenPair(accessToken, refreshToken);
    }

    public String generateAccessToken(Authentication authentication) {
        String claim = "access";
        return generateToken(authentication, claim, 86400000);
    }

    public String generateRefreshToken(Authentication authentication) {
        String claim = "refresh";
        return generateToken(authentication, claim, 864000000);
    }

    public String generateToken(Authentication authentication, String tokenType, int expiration) {

        UserDetails principal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(principal.getUsername())
                .issueTime(now)
                .expirationTime(expiryDate)
                .claim("tokenType", tokenType)
                .build();

        try {
            JWSSigner signer = new RSASSASigner(privateKey);
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID(privateKey.getKeyID())
                            .build(),
                    claimsSet
            );
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRefreshToken(String token) {
        boolean isRefreshToken = false;
        if (extractAllClaims(token).isPresent())
            isRefreshToken = extractAllClaims(token).get().getClaim("tokenType").equals("refresh");
        return isRefreshToken;
    }

    public boolean validateTokenForUser(String token, UserDetails userDetails) {
        var optionalUsername = extractUsername(token);
        return optionalUsername.map(value -> value.equals(userDetails.getUsername())).orElse(false);
    }

    public boolean isValidToken(String token) {
        return extractAllClaims(token).isPresent();
    }

    public Optional<String> extractUsername(String token) {
        if (extractAllClaims(token).isPresent())
            return Optional.ofNullable(extractAllClaims(token).get().getSubject());
        return Optional.empty();
    }

    public Optional<JWTClaimsSet> extractAllClaims(String token) {
        try {
            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            JWT jwt = JWTParser.parse(token);
            if(jwt instanceof SignedJWT) {
                boolean verified = ((SignedJWT) jwt).verify(verifier);
                if (verified) {
                    return Optional.ofNullable(jwt.getJWTClaimsSet());
                }
            }
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
