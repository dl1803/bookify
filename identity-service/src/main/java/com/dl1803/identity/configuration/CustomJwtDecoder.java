package com.dl1803.identity.configuration;

import java.text.ParseException;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.dl1803.identity.service.AuthenticationService;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
// Xóa: vì đã cấu hình Api gateway xử lí
//        try {
//            var response = authenticationService.introspect(
//                    IntrospectRequest.builder().token(token).build());
//
//            if (!response.isValid())
//                throw new JwtException(
//                        "Token invalid!");
//
//        }
//        catch (JOSEException
//                | ParseException
//                        e) {
//            throw new JwtException(
//                    e.getMessage());
//        }
//
//        if (Objects.isNull(nimbusJwtDecoder)) {
//            SecretKeySpec spec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//            nimbusJwtDecoder = NimbusJwtDecoder
//                    .withSecretKey(spec)
//                    .macAlgorithm(MacAlgorithm.HS512)
//                    .build();
//        }
//
//        return nimbusJwtDecoder.decode(
//                token);
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return new Jwt(token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims()
                    );
        } catch (ParseException e) {
            throw new JwtException("Invalid token");
        }
    }
}
