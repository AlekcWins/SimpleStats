package com.simple.stats.datastorage;

import java.util.Optional;

public interface IStorageData<T, V> {

    String TAG_NAME = "simpleStats";

    Optional<T> readData(String key);

    void saveData(T data, String key);

    void setContextObject(V obj);

    V getContextObject();

}
