package vn.edu.hcmute.mp.g4mediaplayer.api.model;

import com.google.gson.annotations.SerializedName;

public class Song {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("fileName")
    private String fileName;

    @SerializedName("version")
    private int version;

    @SerializedName("artist")
    private String artist;

    @SerializedName("size")
    private long size;

    @SerializedName("idUserUpload")
    private String idUserUpload;

    public Song() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getIdUserUpload() {
        return idUserUpload;
    }

    public void setIdUserUpload(String idUserUpload) {
        this.idUserUpload = idUserUpload;
    }
}