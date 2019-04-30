package vn.edu.hcmute.mp.g4mediaplayer.model.entity;

/**
 * Refer the folowwing page showing how to extract song metedata:
 * http://mrbool.com/how-to-extract-meta-data-from-media-file-in-android/28130
 */
public class Song {
    private String id;
    private String name;
    private String author;
    private String composer;
    private String bitrate;
    private byte[] image;
    private String filePath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}