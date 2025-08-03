package org.example.cinema_reservation_system.common;
import com.auth0.jwt.algorithms.Algorithm;

public class Constants {
    public static final String CODE_SUCCESS = "000"; //THÀNH CÔNG
    public static final String MESSAGE_SUCCESS = "Success";
    public static final String E400 = "400";

    public static final String SECRET_KEY = "your_secret_key";
    public static final String ISSUER = "auth0";
    public static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000;//15p
    public static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;//7 ngay
    public static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

}
