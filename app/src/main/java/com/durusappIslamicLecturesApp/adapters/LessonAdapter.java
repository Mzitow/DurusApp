package com.durusappIslamicLecturesApp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.durusappIslamicLecturesApp.R;
import com.durusappIslamicLecturesApp.activities.LessonsActivity;

import com.durusappIslamicLecturesApp.models.ScholarsModel;
import com.durusappIslamicLecturesApp.network.ApiEndPoints;

import com.durusappIslamicLecturesApp.network.RetrofitBuilder;
import com.durusappIslamicLecturesApp.utils.DownloadTask;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.shunan.circularprogressbar.CircularProgressBar;



import java.io.File;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    Context context;
    List<ScholarsModel.DarAudio> lessons;
    private final LayoutInflater mInflater;
    String pathToDars;
    int position;
    protected LessonOnClick lessonOnClick;


    public LessonAdapter(Context context, List<ScholarsModel.DarAudio> lessons, String pathToDars, LessonOnClick lessonOnClick) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.pathToDars = pathToDars;
        this.lessonOnClick = lessonOnClick;
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lesson_viewholder, parent, false);
        return new LessonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.ViewHolder holder, int position) {

        holder.title.setText(lessons.get(position).getTitle());
        holder.index.setText(String.valueOf(position + 1));

        holder.circularProgressBar.setVisibility(View.INVISIBLE);

        if ((lessonAudioExistsInExternalDirectory(lessons.get(position).getTitle()))) {
            holder.download.setVisibility(View.GONE);
            holder.downloadComplete.setVisibility(View.VISIBLE);
        }

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((lessonAudioExistsInExternalDirectory(lessons.get(position).getTitle()))) {
                    holder.play.setVisibility(View.INVISIBLE);
                    holder.pause.setVisibility(View.VISIBLE);
                    lessonOnClick.onPlayClick(lessons.get(position));
                }
                else
                {
                    Toasty.info(context, "للاستماع إلى الملف ، يرجى تنزيله أولاً.", 4000).show();
                }
            }
        });


        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((lessonAudioExistsInExternalDirectory(lessons.get(position).getTitle()))) {
                    holder.play.setVisibility(View.VISIBLE);
                    holder.pause.setVisibility(View.INVISIBLE);
                    lessonOnClick.onPauseClick(lessons.get(position));
                }
            }
        });

        int currentProgress;



        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lessonAudioExistsInExternalDirectory(lessons.get(position).getTitle())) {
                    holder.circularProgressBar.setProgress(0);
                    holder.circularProgressBar.setVisibility(View.VISIBLE);

                    Ion.with(context)
                            .load(lessons.get(position).getImages().get(0))
                            .progress(new ProgressCallback() {
                                @Override
                                public void onProgress(long downloaded, long total) {

                                        double myp = (int) downloaded / (int) total;
                                       int myNo = (int) (myp * 100);
                                    DownloadTask.setProgress(myNo);
                                    

                            }})
                            .write(new File(pathToDars + "/" + lessons.get(position).getTitle()))
                            .setCallback(new FutureCallback<File>() {
                                @Override
                                public void onCompleted(Exception e, File file) {
                                   Toasty.success(context, "Done", Toasty.LENGTH_LONG).show();
                                   holder.circularProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });


                }
            }
        });



    }

    private void downLoadAudioFile(ScholarsModel.DarAudio audio) {


        String baseUrl = audio.getImages().get(0).concat("/") ;

         {
            final ApiEndPoints requestInterface = RetrofitBuilder.getRetrofitInstance().create(ApiEndPoints.class);
            Call<ResponseBody> call = requestInterface.downloadFile(baseUrl);

            call.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                boolean writtenToDisk = LessonsActivity.writeResponseBodyToDisk(response.body(), pathToDars, audio.getTitle());
                                return null;

                            }
                        }.execute();
                    }

                    //LessonAdapter.this.notifyAll();
                    Toasty.success(context,  "تم تنزيل الملف بنجاح", Toasty.LENGTH_SHORT).show();

                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        position = lessons.size();
        return lessons.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;
        TextView index, title, progressPercentage;
        ImageView download, downloadComplete, play, pause;
        CardView downloadCard;
        CircularProgressBar circularProgressBar;
        ProgressBar progressBar1;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar1 = itemView.findViewById(R.id.progrss);
            circularProgressBar =   itemView.findViewById(R.id.circularProgressBar);
            downloadCard = itemView.findViewById(R.id.cv_dowload_progress);
            progressBar = itemView.findViewById(R.id.progressBar);
            index = itemView.findViewById(R.id.tv_lesson_index);
            title = itemView.findViewById(R.id.tv_lesson_title);
            progressPercentage = itemView.findViewById(R.id.tv_progress_update);
            download = itemView.findViewById(R.id.iv_download);
            downloadComplete = itemView.findViewById(R.id.iv_download_complete);
            play = itemView.findViewById(R.id.iv_play);
            pause = itemView.findViewById(R.id.iv_pause);


        }
    }

    private boolean lessonAudioExistsInExternalDirectory(String lessonTitle) {

        String pathToFile = pathToDars + "/" + lessonTitle + ".mp3";
        System.out.println(pathToFile);
        File file = new File(pathToFile);
        return file.exists();

    }

    public interface LessonOnClick {
        void onPauseClick(ScholarsModel.DarAudio audio);

        void onPlayClick(ScholarsModel.DarAudio audio);

        void onDownLoadClick(ScholarsModel.DarAudio audio, LessonAdapter.ViewHolder holder);
    }



}
