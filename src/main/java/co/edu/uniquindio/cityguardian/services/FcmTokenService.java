package co.edu.uniquindio.cityguardian.services;

public interface FcmTokenService {
    void saveToken(String userId, String token) throws Exception;
    String getToken(String userId) throws Exception;
    void deleteToken(String userId) throws Exception;
}