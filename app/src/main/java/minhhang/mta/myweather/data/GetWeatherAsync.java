package minhhang.mta.myweather.data;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.location.Location;
import android.os.AsyncTask;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Minh Hang on 11/25/2016.
 */

public class GetWeatherAsync extends AsyncTask<Void, Void, String> {

    private static final String TAG = "GetWeatherAsnyc";
    private ProgressDialog mProgressDialog;
    private Location location;
    private String body;


    public interface CallBack {
        public void onFinish(String body) throws IOException;
    }


    private CallBack callBack;

    public GetWeatherAsync(Context context, Location location) {
        this.location = location;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Loading.....");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {

        OkHttpClient client = new OkHttpClient();
        Request request1 = new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude()
                        + "&lon=" + location.getLongitude() + "&APPID=900dd5ec4bf97cd8212678256c39e6e1")
                .addHeader("Accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request1).execute();
            if (response.isSuccessful()) {
                body = response.body().string();
                Log.d(TAG, "body = " + body);
                return body;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    @Override
    protected void onPostExecute(String body) {
        super.onPostExecute(body);
        mProgressDialog.dismiss();
        if (callBack != null) {
            try {
                callBack.onFinish(body);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCallBack(CallBack calLBack) {
        this.callBack = calLBack;
    }
}

