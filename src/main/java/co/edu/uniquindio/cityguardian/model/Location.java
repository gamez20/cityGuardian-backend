package co.edu.uniquindio.cityguardian.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Location {
    private String latitude;
    private String longitude;

    @Builder
    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}