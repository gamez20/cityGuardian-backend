package co.edu.uniquindio.cityguardian.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("reports")
@Getter
@Setter
@NoArgsConstructor
public class Report {

    //var
    @Id
    private String id;
    private String title;
    private Category category;
    private String description;
    private Boolean solved;
    private Boolean important;
    private String locationIdFk;
    private ReportStatus status;
    private LocalDateTime creationDate;
    private List<String> comments;
    private int priority;
    private String userId;

    //builder
    @Builder
    public Report(String id, String title, Category category, String description, Boolean solved, Boolean important,String locationIdFk, ReportStatus status, LocalDateTime creationDate, List<String> comments, int priority, String userId) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.solved = solved;
        this.important = important;
        this.locationIdFk = locationIdFk;
        this.status = status;
        this.creationDate = creationDate;
        this.comments = comments;
        this.priority = priority;
        this.userId = userId;
    }



    //methods
    public static void updateStatus(ReportStatus status){}
    public static void assingPriority(){}
    public static void viewHistory(){}

    //getters y setters


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

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

    public String getLocationIdFk() {
        return locationIdFk;
    }

    public void setLocationIdFk(String locationIdFk) {
        this.locationIdFk = locationIdFk;
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
}
