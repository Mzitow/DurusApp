package com.durusappIslamicLecturesApp.utils;


import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

  public  class DownloadTask  {

    private   static  int progress;

    public static int getProgress() {
      return progress;
    }

    public static void setProgress(int progress) {
      DownloadTask.progress = progress;
    }
  }