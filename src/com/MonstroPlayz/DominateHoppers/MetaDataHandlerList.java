package com.MonstroPlayz.DominateHoppers;

import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;

import java.util.concurrent.Callable;

public class MetaDataHandlerList {

    public static <T> T getValue(Metadatable metadatable, String metaData, Class<T> clazz) {
        if (!metadatable.hasMetadata(metaData)) return null;
        MetadataValue metadataValue = metadatable.getMetadata(metaData).get(0);
        Object object = metadataValue.value();
        return object != null ? (T) metadataValue.value() : null;
    }

    public static <T> T getValue(Metadatable metadatable, MetaDataKey metaData, Class<T> clazz) {
        return getValue(metadatable, metaData.getMetaDataKey(), clazz);
    }

    public static void setValue(Metadatable metadatable,String metaData,Object value) {
        if (metaData != null) metadatable.setMetadata(metaData,new LazyMetadataValue(Main.getInstance(),
                LazyMetadataValue.CacheStrategy.NEVER_CACHE, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return value;
            }
        }));
    }

    public static void setValue(Metadatable metadatable,MetaDataKey metaDataKey,Object value) {
        setValue(metadatable,metaDataKey.getMetaDataKey(),value);
    }

    public static boolean hasMetaData(Metadatable metadatable,String key) {
        return metadatable.hasMetadata(key);
    }

    public static boolean hasMetaData(Metadatable metadatable,MetaDataKey metaDataKey) {
        return hasMetaData(metadatable,metaDataKey.getMetaDataKey());
    }

}
