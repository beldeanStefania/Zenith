//package com.ubb.zenith.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JwtService {
//    //private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//
//    private String secretKey = "";
//
//    public JwtService() throws NoSuchAlgorithmException {
//        try{
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//        SecretKey scKey = keyGenerator.generateKey();
//       secretKey = Base64.getEncoder().encodeToString(scKey.getEncoded());}
//        catch (NoSuchAlgorithmException e){
//            throw new NoSuchAlgorithmException("No such algorithm found");
//        }
//    }
//
//    public String generateToken(UserDetails userDetails) {
//        return generateToken(new HashMap<>(), userDetails);
//    }
//
//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return Jwts
//                .builder()
//                .claims(extraClaims)
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
//                .signWith(getSignInKey())
//                .compact();
//    }
//
//     public String extractUsername(String token) {
//     return extractClaim(token, Claims::getSubject);
//         }
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(getSignInKey())
//                .build().parseSignedClaims(token).getPayload();
//    }
//   private SecretKey getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decodes the specified Base64 encoded String and returns a byte array
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//}
