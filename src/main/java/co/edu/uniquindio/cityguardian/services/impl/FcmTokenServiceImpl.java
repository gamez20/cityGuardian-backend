package co.edu.uniquindio.cityguardian.services.impl;

import co.edu.uniquindio.cityguardian.services.FcmTokenService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FcmTokenServiceImpl implements FcmTokenService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveToken(String userId, String token) throws Exception {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("El token FCM no puede estar vacío");
        }

        Query query = Query.query(Criteria.where("userId").is(userId));
        Update update = new Update()
                .set("token", token)
                .set("updatedAt", LocalDateTime.now());

        mongoTemplate.upsert(query, update, "fcm_tokens");
    }

    @Override
    public String getToken(String userId) throws Exception {
        Query query = Query.query(Criteria.where("userId").is(userId));
        Document doc = mongoTemplate.findOne(query, Document.class, "fcm_tokens");

        if (doc == null) {
            return null;
        }

        return doc.getString("token");
    }

    @Override
    public void deleteToken(String userId) throws Exception {
        Query query = Query.query(Criteria.where("userId").is(userId));
        mongoTemplate.remove(query, "fcm_tokens");
    }
}