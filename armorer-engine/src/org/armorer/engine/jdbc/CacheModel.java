package org.armorer.engine.jdbc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class CacheModel {

    /**
     * Constant to turn off periodic cache flushes
     */
    private static final long NO_FLUSH_INTERVAL = -99999;
    public static final Object NULL_OBJECT = new String("SERIALIZABLE_NULL_OBJECT");

    private int requests = 0;
    private int hits = 0;

    private String id;
    private String type;
    private boolean readOnly;
    private boolean serialize;
    private long lastFlush;
    private long flushInterval;
    private long flushIntervalSeconds;
    private List<String> flushOnExecuteList;

    private MemoryCacheController controller;

    public Object getObject(String key) {
        Object value = null;
        synchronized (this) {
            if (flushInterval != NO_FLUSH_INTERVAL && System.currentTimeMillis() - lastFlush > flushInterval) {
                flush();
            }
        }
        value = controller.getObject(key);
        if (serialize && !readOnly && (value != NULL_OBJECT && value != null)) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) value);
                ObjectInputStream ois = new ObjectInputStream(bis);
                value = ois.readObject();
                ois.close();
            } catch (Exception e) {
                throw new RuntimeException(
                        "Error caching serializable object.  Be sure you're not attempting to use "
                                + "a serialized cache for an object that may be taking advantage of lazy loading.  Cause: "
                                + e, e);
            }
        }
        requests++;
        if (value != null) {
            hits++;
        }
        return value;
    }

    /**
     * Add an object to the cache
     * 
     * @param key The key of the object to be cached
     * @param value The object to be cached
     */
    public void putObject(String key, Object value) {
        if (null == value)
            value = NULL_OBJECT;
        synchronized (this) {
            if (serialize && !readOnly && value != NULL_OBJECT) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(value);
                    oos.flush();
                    oos.close();
                    value = bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("Error caching serializable object.  Cause: " + e, e);
                }
            }
            controller.putObject(key, value);
        }
    }

    /**
     * Clears the cache
     */
    public void flush() {
        synchronized (this) {
            controller.flush();
            lastFlush = System.currentTimeMillis();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isSerialize() {
        return serialize;
    }

    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }

    public long getLastFlush() {
        return lastFlush;
    }

    public void setLastFlush(long lastFlush) {
        this.lastFlush = lastFlush;
    }

    public long getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(long flushInterval) {
        this.flushInterval = flushInterval;
    }

    public long getFlushIntervalSeconds() {
        return flushIntervalSeconds;
    }

    public void setFlushIntervalSeconds(long flushIntervalSeconds) {
        this.flushIntervalSeconds = flushIntervalSeconds;
    }

    public List<String> getFlushOnExecuteList() {
        return flushOnExecuteList;
    }

    public void setFlushOnExecuteList(List<String> flushOnExecuteList) {
        this.flushOnExecuteList = flushOnExecuteList;
    }

}
