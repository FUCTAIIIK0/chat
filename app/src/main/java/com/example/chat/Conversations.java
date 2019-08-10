package com.example.chat;

public class Conversations {
    public boolean seen;
    public long timestamp;

    public  Conversations(){
    }

    public boolean isSeen() {
        return seen;
    }
    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Conversations(boolean seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }
}
