package com.uniquestudio.technorange;

public class BannerContent {
    private int BannerHeading, BannerDescript, BannerImg ;
    private String BannerBtnTxt;

    public BannerContent(int bannerHeading, int bannerDescript, String bannerBtnTxt, int bannerImg) {
        BannerHeading = bannerHeading;
        BannerDescript = bannerDescript;
        BannerBtnTxt = bannerBtnTxt;
        BannerImg = bannerImg;
    }

    public int getBannerHeading() {
        return BannerHeading;
    }

    public void setBannerHeading(int bannerHeading) {
        BannerHeading = bannerHeading;
    }

    public int getBannerDescript() {
        return BannerDescript;
    }

    public void setBannerDescript(int bannerDescript) {
        BannerDescript = bannerDescript;
    }

    public String getBannerBtnTxt() {
        return BannerBtnTxt;
    }

    public void setBannerBtnTxt(String  bannerBtnTxt) {
        BannerBtnTxt = bannerBtnTxt;
    }

    public int getBannerImg() {
        return BannerImg;
    }

    public void setBannerImg(int bannerImg) {
        BannerImg = bannerImg;
    }
}
