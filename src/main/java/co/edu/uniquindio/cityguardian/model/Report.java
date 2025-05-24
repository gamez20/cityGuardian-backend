package co.edu.uniquindio.cityguardian.model;

// ...existing imports...

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Document("reports")
@Getter
@Setter
@NoArgsConstructor
public class Report {
    @Id
    private String id;
    private String title;
    private String description;
    private Boolean solved;
    private Boolean important;
    private Category category;
    private ReportStatus status = ReportStatus.CREATED;
    private LocalDateTime creationDate;
    private List<String> comments;
    private int priority;
    private List<String> imageUrls;
    private String userId;
    @Field("reject_reason")
    private String rejectReason;
    private Location location;

    @Builder
    public Report(String id, String title, String description, Boolean solved,
            Boolean important, Category category, ReportStatus status,
            LocalDateTime creationDate, List<String> comments, int priority,
            List<String> imageUrls, String userId, Location location) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.solved = solved;
        this.important = important;
        this.category = category;
        this.status = status;
        this.creationDate = creationDate;
        this.comments = comments;
        this.priority = priority;
        this.userId = userId;
        this.imageUrls = imageUrls;
        this.location = location;
    }

    // methods
    public static void updateStatus(ReportStatus status) {
    }

    public static void assingPriority() {
    }

    public static void viewHistory() {
    }

    // getters y setters

    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public Boolean getImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
