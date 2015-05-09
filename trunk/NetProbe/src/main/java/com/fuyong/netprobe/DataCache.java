package com.fuyong.netprobe;

import com.fuyong.netprobe.common.QID;

import java.util.HashMap;
import java.util.List;

/**
 * Created by democrazy on 2014/6/14.
 */
public class DataCache {
    private static DataCache instance;
    private HashMap<QID.ID, Object> mCacheObject = new HashMap<QID.ID, Object>();
    private HashMap<QID.ID, Number> mCacheNum = new HashMap<QID.ID, Number>();

    private DataCache() {
    }

    synchronized public static DataCache getInstance() {
        if (null == instance) {
            instance = new DataCache();
        }
        return instance;
    }

    synchronized public DataCache getSnapshot() {
        DataCache cache = new DataCache();
        cache.mCacheNum.putAll(this.mCacheNum);
        cache.mCacheObject.putAll(this.mCacheObject);
        return cache;
    }

    synchronized public void release() {
        mCacheObject.clear();
        mCacheNum.clear();
        instance = null;
    }

    synchronized public Object get(QID.ID k) {
        return mCacheObject.get(k);
    }

    synchronized public Object get(QID.ID k, int i) {
        List<?> list = (List<?>) get(k);
        if (null == list) {
            return null;
        }
        return list.get(i);
    }

    synchronized public Number getNumber(QID.ID k) {
        return mCacheNum.get(k);
    }

    synchronized public Number getNumber(QID.ID k, int i) {
        List<Number> list = (List<Number>) get(k);
        if (null == list) {
            return null;
        }
        return list.get(i);
    }

    synchronized public void put(QID.ID k, Object v) {
        mCacheObject.put(k, v);
    }

    synchronized public void clear() {
        mCacheObject.clear();
        mCacheNum.clear();
    }

    synchronized public void put(QID.ID k, byte v) {
        mCacheNum.put(k, v);
    }

    synchronized public void put(QID.ID k, short v) {
        mCacheNum.put(k, v);
    }

    synchronized public void put(QID.ID k, int v) {
        mCacheNum.put(k, v);
    }

    synchronized public void put(QID.ID k, long v) {
        mCacheNum.put(k, v);
    }

    synchronized public void put(QID.ID k, float v) {
        mCacheNum.put(k, v);
    }

    synchronized public void put(QID.ID k, byte v, int i) {
        List<Byte> obj = (List<Byte>) mCacheObject.get(k);
        if (null != obj) {
            obj.set(i, v);
            return;
        }
    }

    synchronized public void put(QID.ID k, short v, int i) {
        mCacheNum.put(k, v);
    }

    synchronized public void put(QID.ID k, int v, int i) {
        mCacheNum.put(k, v);
    }

    synchronized public void put(QID.ID k, long v, int i) {
        mCacheNum.put(k, v);
    }

    synchronized public void put(QID.ID k, float v, int i) {
        mCacheNum.put(k, v);
    }

}
