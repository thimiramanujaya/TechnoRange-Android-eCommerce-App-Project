package com.uniquestudio.technorange;

public class Products {

    private String pid, ptitle, image, price, avail_quantity, description, date, time;

    public Products() {

    }

    public Products(String pid, String ptitle, String image, String price, String avail_quantity, String description, String date, String time) {
        this.pid = pid;
        this.ptitle = ptitle;
        this.image = image;
        this.price = price;
        this.avail_quantity = avail_quantity;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvail_quantity() {
        return avail_quantity;
    }

    public void setAvail_quantity(String avail_quantity) {
        this.avail_quantity = avail_quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
