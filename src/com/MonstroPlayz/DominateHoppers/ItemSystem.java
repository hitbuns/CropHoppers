package com.MonstroPlayz.DominateHoppers;

import com.MenuAPI.Configs.iItemStackPath;

public enum ItemSystem implements iItemStackPath {


    CROP_HOPPER_ITEM("items.cropHopperItem")
    ;

    ItemSystem(String path) {
        this.path = path;
    }

    private final String path;


    @Override
    public String getPath() {
        return path;
    }
}
