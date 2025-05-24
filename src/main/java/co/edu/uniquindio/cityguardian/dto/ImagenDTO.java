package co.edu.uniquindio.cityguardian.dto;

public class ImagenDTO {
    private String url;
    private String id;

    public ImagenDTO() {
    }

    public ImagenDTO(String url, String id) {
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}