package vn.edu.hcmute.mp.g4mediaplayer.model.entity;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public class PlayList implements Serializable {
    private String id;
    private String name;
    private Date createdDate;
    private byte[] image;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
