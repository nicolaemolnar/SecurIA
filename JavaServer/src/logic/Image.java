package logic;

import java.sql.Timestamp;

public class Image {
    private String path;
    private String label;
    private Timestamp timestamp;

    public Image(String path, String label, Timestamp timestamp) {
        this.path = path;
        this.label = label;
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public String getLabel() {
        return label;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Image{" +
                "path='" + path + '\'' +
                ", label='" + label + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
