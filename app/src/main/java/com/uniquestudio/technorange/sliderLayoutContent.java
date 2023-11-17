package com.uniquestudio.technorange;

public class sliderLayoutContent {

    int Heading, Descript, ScreenImg;

    public sliderLayoutContent(int heading, int descript, int screenImg) {
        this.Heading = heading;
        this.Descript = descript;
        this.ScreenImg = screenImg;
    }

    public int getHeading() {
        return Heading;
    }

    public int getDescript() {
        return Descript;
    }

    public int getScreenImg() {
        return ScreenImg;
    }

    public void setHeading(int heading) {
        Heading = heading;
    }

    public void setDescript(int descript) {
        Descript = descript;
    }

    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }
}
