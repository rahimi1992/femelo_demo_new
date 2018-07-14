package com.femelo.femelo_demo;

import java.io.Serializable;

class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    private int mId;
    private String mName;
    private int mParent;
    private String mDescription;
    private int mCount;
    private String mImage;

    public Category(int id, String name, int parent, String description, int count, String image) {
        mId = id;
        mName = name;
        mParent = parent;
        mDescription = description;
        mCount = count;
        mImage = image;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getParent() {
        return mParent;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getCount() {
        return mCount;
    }

    public String getImage() {
        return mImage;
    }
}
