package auth;

import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

public class JWTtoken {
    private static final String SECRET_KEY = "calicoBilai";
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final String encodedHeader = encode(JWT_HEADER.getBytes(StandardCharsets.UTF_8));
    private final String encodedPayload;
    private JSONObject payload;
    private String signature;

    public JWTtoken(String Base64EncodedCredentials, int MinutesTillExpiration) throws Exception {
        String username, password;
        {
            String[] decoded = decode(Base64EncodedCredentials).split(":");
            username = decoded[0];
            password = decoded[1];
        }

        payload = new JSONObject();
        payload.put("username", username);
        payload.put("password", password);
        long exp = LocalDateTime.now().plusMinutes(MinutesTillExpiration).toEpochSecond(ZoneOffset.UTC);
        payload.put("expiry_time", exp);

        encodedPayload = encode(payload.toString().getBytes(StandardCharsets.UTF_8));
        signature = HMACS256(encodedHeader + "." + encodedPayload);
        //long remMinutes = (exp - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))/60;
        //System.out.println("Minutes remaining: " + remMinutes);
    }

    public JWTtoken(String stringToken){
        String tokenList[] = stringToken.split(".");

    }

    public String toString(){
        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    private static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    ///Reference: https://github.com/metamug/java-jwt/blob/master/src/main/java/com/metamug/jwt/JWebToken.java#L32
    private String HMACS256(String data)throws Exception{
        try{
            byte[] hash = SECRET_KEY.getBytes(StandardCharsets.UTF_8);

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);

            return encode(sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        }catch (Exception e){
            System.out.println("HMACS256 error: " + e);
            throw new RuntimeException();
        }
    }
}
