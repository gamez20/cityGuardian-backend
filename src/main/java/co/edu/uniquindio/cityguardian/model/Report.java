package co.edu.uniquindio.cityguardian.model;

// ...existing imports...

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

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
    @Field("comments")
    private List<Comment> comments = new ArrayList<>();
    private int priority;
    private List<String> imageUrls;
    private String userId;
    @Field("reject_reason")
    private String rejectReason;
    private Location location;
    private List<ReportStateHistory> stateHistory = new ArrayList<>();

    @Builder
    public Report(String id, String title, String description, Boolean solved,
                  Boolean important, Category category, ReportStatus status,
                  LocalDateTime creationDate, List<Comment> comments, int priority,
                  List<String> imageUrls, String userId, Location location) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.solved = solved;
        this.important = important;
        this.category = category;
        this.status = status;
        this.creationDate = creationDate;
        this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
        this.priority = priority;
        this.userId = userId;
        this.imageUrls = imageUrls;
        this.location = location;
        this.stateHistory = new ArrayList<>();
        if (status != null) {
            this.stateHistory.add(new ReportStateHistory(status, creationDate != null ? creationDate : LocalDateTime.now(), null));
        }
    }

    // methods
    public static void updateStatus(ReportStatus status) {
    }

    public static void assingPriority() {
    }

    public static void viewHistory() {
    }

    public void addStateChange(ReportStatus newStatus, String reason) {
        if (this.stateHistory == null) {
            this.stateHistory = new ArrayList<>();
        }

        // Crear nuevo estado
        ReportStateHistory newStateHistory = new ReportStateHistory(newStatus, LocalDateTime.now(), reason);

        // Agregar a la lista existente
        this.stateHistory.add(newStateHistory);

        // Actualizar estado actual
        this.status = newStatus;

        // Si el nuevo estado es REJECTED, agregar la razón
        if (ReportStatus.REJECTED.equals(newStatus)) {
            this.rejectReason = reason;
        }
        // Si el estado anterior era REJECTED y cambia a otro estado, limpiar la razón
        else if (this.rejectReason != null && !ReportStatus.REJECTED.equals(newStatus)) {
            this.rejectReason = null;
        }
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

    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void setComments(List<Comment> comments) {
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

    public List<ReportStateHistory> getStateHistory() {
        return stateHistory;
    }

    public void setStateHistory(List<ReportStateHistory> stateHistory) {
        this.stateHistory = stateHistory;
    }
}
