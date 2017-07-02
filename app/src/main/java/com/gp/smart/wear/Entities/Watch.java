package com.gp.smart.wear.Entities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by basse on 22-Dec-16.
 */

@IgnoreExtraProperties
public class Watch implements Serializable {
    private String title;
    private String description;
    private String id;
    private String operating_system;
    private String chipset;
    private String cpu;
    private String ram;
    private String storage;
    private String battery;
    private String sensors;
    private String album_id;
    private String photo;
    private String price;
    private List<String> links;

    public Watch() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getOperating_system() {
        return operating_system;
    }

    public void setOperating_system(String operating_system) {
        this.operating_system = operating_system;
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getSensors() {
        return sensors;
    }

    public void setSensors(String sensors) {
        this.sensors = sensors;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}