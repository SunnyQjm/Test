package com.sunny.test.internet;

/**
 * Created by Sunny on 2017/8/9 0009.
 */

public class FileDownloadEvent {
    private final long total;
    private final long already;
    private final boolean done;

    private FileDownloadEvent(Builder builder) {
        total = builder.total;
        already = builder.already;
        done = builder.done;
    }

    @Override
    public String toString() {
        return "FileDownloadEvent{" +
                "total=" + total +
                ", already=" + already +
                ", done=" + done +
                '}';
    }

    public long getAlready() {
        return already;
    }

    public long getTotal() {
        return total;
    }

    public boolean isDone() {
        return done;
    }

    public static final class Builder {
        private long total;
        private long already;
        private boolean done;

        public Builder() {
        }

        public Builder total(long val) {
            total = val;
            return this;
        }

        public Builder already(long val) {
            already = val;
            return this;
        }

        public Builder done(boolean val) {
            done = val;
            return this;
        }

        public FileDownloadEvent build() {
            return new FileDownloadEvent(this);
        }
    }
}
