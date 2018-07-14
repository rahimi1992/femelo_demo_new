package com.femelo.femelo_demo;

import java.io.Serializable;

class Product implements Serializable{
    private static final long serialVersionUID = 1L;
    private int mId;
    private String mName;
    private String mDescription;
    private String mShortDesc;
    private String mStatus;
    private String mImage;
    private String mPrice;

    public Product(int id, String name, String description, String shortDesc, String status, String image, String price) {
        mId = id;
        mName = name;
        mDescription = description;
        mShortDesc = shortDesc;
        mStatus = status;
        mImage = image;
        mPrice = price;

    }

    int getId() {
        return mId;
    }

    String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    String getStatus() {
        return mStatus;
    }

    String getImage() {
        return mImage;
    }

    String getPrice() {
        return mPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mShortDesc='" + mShortDesc + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mPrice='" + mPrice + '\'' +
                '}';
    }
}
