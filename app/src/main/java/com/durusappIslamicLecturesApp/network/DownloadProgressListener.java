package com.durusappIslamicLecturesApp.network;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}