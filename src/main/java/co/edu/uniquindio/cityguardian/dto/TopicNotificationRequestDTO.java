package co.edu.uniquindio.cityguardian.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class TopicNotificationRequestDTO {
    private String topic;
    private String title;
    private String body;

    public TopicNotificationRequestDTO(String topic, String title, String body) {
        this.topic = topic;
        this.title = title;
        this.body = body;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}