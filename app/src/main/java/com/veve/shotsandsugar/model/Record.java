package com.veve.shotsandsugar.model;

public class Record {

    public Record(Object originalRecord, long timestamp, String text) {
        this.originalRecord = originalRecord;
        this.timestamp = timestamp;
        this.text = text;
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
