package com.MonstroPlayz.DominateHoppers;

public enum MetaDataKey implements iMetaDataKey{

    IS_A_CROP_HOPPER("cropHopper"),
    CROP_HOPPER_OWNER_UUID("cropHopperOwnerUUID")
    ;

    MetaDataKey(String key) {
        this.key = key;
    }

    private final String key;

    @Override
    public String getMetaDataKey() {
        return this.key;
    }
}
