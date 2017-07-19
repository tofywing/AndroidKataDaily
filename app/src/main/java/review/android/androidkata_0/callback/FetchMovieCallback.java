package review.android.androidkata_0.callback;

/**
 * Created by Yee on 7/8/17.
 */

public interface FetchMovieCallback {

    void onFetchTaskSuccess(String json);

    void onFetchTaskFailed();
}
