package com.veve.shotsandsugar.model;

import android.support.annotation.NonNull;

public class Record implements Comparable {

    public Record(Object originalRecord, long timestamp, String text) {
        this.originalRecord = originalRecord;
        this.timestamp = timestamp;
        this.text = text;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Record compareTo = (Record)o;
        int result = 0;
        if (timestamp > compareTo.getTimestamp()) {
            result = 1;
        } else if (timestamp == compareTo.getTimestamp()) {
            result = 0;
        } else if (timestamp < compareTo.getTimestamp()) {
            result = -1;
        }
        return result;
    }

    public Object getOriginalRecord() {
        return originalRecord;
    }

    public void setOriginalRecord(Object originalRecord) {
        this.originalRecord = originalRecord;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    Object originalRecord;

    long timestamp;

    String text;

}
