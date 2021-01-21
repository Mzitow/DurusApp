package com.durusappIslamicLecturesApp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.durusappIslamicLecturesApp.R;
import com.durusappIslamicLecturesApp.adapters.LessonAdapter;
import com.durusappIslamicLecturesApp.models.ScholarsModel;
import com.durusappIslamicLecturesApp.network.ApiEndPoints;
import com.durusappIslamicLecturesApp.network.RetrofitBuilder;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.durusappIslamicLecturesApp.utils.Constants.EXTERNAL_FOLDER_NAME;

public class LessonsActivity extends AppCompatActivity implements LessonAdapter.LessonOnClick {

    ScholarsModel.Dar currentDar;
    TextView courseName, noLessons;
    RecyclerView rv_lessons;
    String path = "";

    LessonAdapter lessonAdapter;
    SpinKitView spinKitView;
    public static MediaPlayer player;

    MutableLiveData<Boolean> isplayAudio = new MutableLiveData<>();
    ScholarsModel.DarAudio audioFile ;

    PlayerView simpleExoPlayer;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 5689;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static boolean permissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        checkPermissions();
        createExternalDirectory();


        SharedPreferences mPreferences = getSharedPreferences("currentScholar", MODE_PRIVATE);
        Gson gson = new Gson();
        String dars = mPreferences.getString("currentdars", "");
        currentDar = gson.fromJson(dars, ScholarsModel.Dar.class);

        courseName = findViewById(R.id.tv_course_name);
        spinKitView = findViewById(R.id.spin_kit_3);
        noLessons = findViewById(R.id.tv_no_lessons);
        rv_lessons = findViewById(R.id.rv_lessons);
        simpleExoPlayer = findViewById(R.id.simple_media_view);
//        String uri = currentDar.getDarAudio().get(1).getImages().get(0);
//        String fileTitle = currentDar.getDarAudio().get(1).getTitle();
        //downloadWithRetrofit(uri, getDarsFolder(currentDar.getDarsTitle()), fileTitle);

        SimpleExoPlayer player = new SimpleExoPlayer.Builder(LessonsActivity.this).build();


        noLessons.setVisibility(View.GONE);
        spinKitView.setVisibility(View.GONE);

        courseName.setText(currentDar.getDarsTitle());

        if (currentDar.getDarAudio().size() > 0) {

            sendLessonsToRecycler(currentDar.getDarAudio(), getDarsFolder(currentDar.getDarsTitle()));
        }
        else {
            noLessons.setVisibility(View.VISIBLE);
        }


        isplayAudio.observe(LessonsActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                System.out.println("Observing");
                System.out.println(audioFile.getTitle());
                toogleAudioFile(aBoolean);
            }
        });

    }

    private void toogleAudioFile(Boolean aBoolean) {

        String audioUrl =   getDarsFolder(currentDar.getDarsTitle()) + "/" +  audioFile.getTitle() +".mp3";

        File file = new File(audioUrl);
        if (file.exists())
            player =  MediaPlayer.create(LessonsActivity.this, Uri.parse(audioUrl));

        if (aBoolean) {

            if (!player.isPlaying())
            {
                player.start();
            }

        }
        else
        {
            player.pause();
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {


            }
        });

    }

    private void downloadWithRetrofit(String s, String pathToDars, String filename) {

        final ApiEndPoints requestInterface = RetrofitBuilder.getRetrofitInstance().create(ApiEndPoints.class);
                Call<ResponseBody> call = requestInterface.downloadFile(s);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(), pathToDars, filename);

                            return null;
                        }
                    }.execute();
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    protected void checkPermissions () {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions.toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                permissionGranted = true;
                break;
        }
    }

    private String createExternalDirectory() {
       if (permissionGranted)
       {
           File file = new File(Environment.getExternalStorageDirectory(), EXTERNAL_FOLDER_NAME);
           if (!file.exists()) {
               file.mkdirs();
               path = file.getAbsolutePath();
           } else {
               path = file.getAbsolutePath();
           }

           return path;
       }
       else
           return "";
    }

    private String getDarsFolder(String darsName) {
        String pathToDars;
        if (permissionGranted)
        {
            File file = new File(path , darsName );
            if (!file.exists()) {
                file.mkdirs();
                pathToDars = file.getAbsolutePath();
            } else {
                pathToDars = file.getAbsolutePath();
            }
            return pathToDars;
        }
        else
            return "";
    }

    private void sendLessonsToRecycler(List<ScholarsModel.DarAudio> darAudio, String darsPath) {

         lessonAdapter = new LessonAdapter(LessonsActivity.this, darAudio, darsPath, this);
        rv_lessons.setLayoutManager(new GridLayoutManager(LessonsActivity.this, 1));
        rv_lessons.setHasFixedSize(true);
        rv_lessons.setAdapter(lessonAdapter);
    }

    public static boolean writeResponseBodyToDisk(ResponseBody body, String pathToDars, String fileName) {
        try {
            // todo change the file location/name according to your needs

            File futureStudioIconFile = new File(pathToDars,fileName + ".mp3");
            Log.d("path", "writeResponseBodyToDisk: " + pathToDars);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void onPauseClick(ScholarsModel.DarAudio audio) {

            player.release();
            isplayAudio.setValue(false);
            System.out.println("Pause Click");

    }

    @Override
    public void onPlayClick(ScholarsModel.DarAudio audio) {

            audioFile = audio;
            isplayAudio.setValue(true);
            System.out.println("Play Click");

    }

    @Override
    public void onDownLoadClick(ScholarsModel.DarAudio audio, LessonAdapter.ViewHolder holder) {



        //downloadWithRetrofit(audio.getImages().get(0), getDarsFolder(currentDar.getDarsTitle()), audio.getTitle().concat(".mp3"));
    }


}

