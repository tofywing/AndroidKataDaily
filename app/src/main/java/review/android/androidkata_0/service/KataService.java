package review.android.androidkata_0.service;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import review.android.androidkata_0.R;
import review.android.androidkata_0.callback.FetchMovieCallback;
import review.android.androidkata_0.model.MoviesLab;

import static review.android.androidkata_0.KataActivity.SEARCH_INPUT;
import static review.android.androidkata_0.manager.ScreenAppearanceManager.SHARED_PREFERENCE;

/**
 * Created by Yee on 7/8/17.
 */

public class KataService extends Service implements FetchMovieCallback {
    public static final String TAG = "KataService";
    public static final String SUCCESS = "movies are fetched";
    public static final String DATA = "movies data";
    public static final String JSON = "fetched movie data";

    static Context mContext;
    FetchMoviesTask mFetchMoviesTask;

    public static Intent newIntent(Context context) {
        mContext = context;
        return new Intent(mContext, KataService.class);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mFetchMoviesTask = new FetchMoviesTask(mContext, this);
        assert intent != null;
        String input = intent.getStringExtra(SEARCH_INPUT);
        mFetchMoviesTask.execute(input);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onFetchTaskSuccess(String json) {
        Intent intent = new Intent(KataService.SUCCESS);
        MoviesLab moviesLab = new MoviesLab();
        moviesLab.initMovies(json);
        intent.putParcelableArrayListExtra(DATA, moviesLab.getMovies());
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(JSON, json);
        editor.apply();
        sendBroadcast(intent);
    }

    @Override
    public void onFetchTaskFailed() {

    }

    @Override
    public boolean stopService(Intent name) {
        if (mFetchMoviesTask != null) {
            mFetchMoviesTask.cancel(true);
        }
        return super.stopService(name);
    }

}
