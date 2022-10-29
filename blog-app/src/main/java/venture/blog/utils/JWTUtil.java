package venture.blog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class JWTUtil {
    // 签名密钥
    private static final String SECRET;
    static {
        try {
            InputStream inputStream = new ClassPathResource("global.properties").getInputStream();
            Properties properties = new Properties();
            properties.load(inputStream);
            SECRET = (String)properties.get("login.tokensecret");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static com.auth0.jwt.algorithms.Algorithm Algorithm;

    /**
     * 生成token
     *
     * @param payload token携带的信息
     * @return token字符串
     */
    public static String getToken(Map<String, String> payload) {
        // 指定token过期时间为2天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);

        JWTCreator.Builder builder = JWT.create();
        // 构建payload
        payload.forEach((k, v) -> builder.withClaim(k, v));
        // 指定过期时间和签名算法
        String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(SECRET));
        return token;
    }


    /**
     * 解析token
     *
     * @param token token字符串
     * @return 解析后的token
     */
    public static DecodedJWT decode(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT;
    }

    public static boolean verify(String token) {
        try {
            // 1.校验登录JWT字符串
            DecodedJWT decodedJWT = decode(token);
            return true;
        } catch (SignatureVerificationException e) {
            log.info("无效签名");
            e.printStackTrace();
        } catch (TokenExpiredException e) {
            log.info("token已经过期");
            e.printStackTrace();
        } catch (AlgorithmMismatchException e) {
            log.info("算法不一致");
            e.printStackTrace();
        } catch (Exception e) {
            log.info("token无效");
            e.printStackTrace();
        }
        return false;
    }
}