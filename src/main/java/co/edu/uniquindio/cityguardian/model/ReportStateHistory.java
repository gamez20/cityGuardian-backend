package co.edu.uniquindio.cityguardian.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor

public class ReportStateHistory {
    private ReportStatus status;
    private LocalDateTime date;
    private String reason;

    public ReportStateHistory(ReportStatus status, LocalDateTime date, String reason) {
        this.status = status;
        this.date = date;
        this.reason = reason;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
