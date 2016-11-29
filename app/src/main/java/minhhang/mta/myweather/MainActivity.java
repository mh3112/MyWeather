package minhhang.mta.myweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import minhhang.mta.myweather.Model.example.WeatherEntity;
import minhhang.mta.myweather.data.GetWeatherAsync;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "MainActivity";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    protected TextView tv_location;
    protected TextView tv_temperature;
    protected ImageView img_View;

    private GetWeatherAsync currentWeather;
    private File file;
    private String path;

    private GoogleApiClient client;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_location = (TextView) findViewById((R.id.tv_location));
        tv_temperature = (TextView) findViewById((R.id.tv_temperature));
        img_View = (ImageView) findViewById(R.id.img_View);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/current_weather.json";
        buildGoogleApiClient();
        //Check self permission WRITE_EXTERNAL_STORAGE với android 6.0 trở lên
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            }
        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        file = new File(path);
        Log.e("PATH", path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_ACCESS_COURSE_FINE_LOCATION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }else{

                }
                break;
            }
            case REQUEST_WRITE_STORAGE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    file = new File(path);
                }else{

                }
                break;
            }
        }
    }

    private void getCurrentLocation() {
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation == null){

            }
        }catch (SecurityException e){
            Toast.makeText(this, "Show My Location Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


        /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {getWeather(mLastLocation);

        } else {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
            
        }
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Lỗi kết nối: " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    public void getWeather(Location currentLocation) {
        currentWeather = new GetWeatherAsync(this, currentLocation);
        currentWeather.execute();
        currentWeather.setCallBack(new GetWeatherAsync.CallBack() {

            @Override
            public void onFinish(String body) throws IOException {
                try {
                    Log.e("Callback", body);
                    Gson gson = new Gson();

         //           FileWriter fileWriter = new FileWriter(file);
                //    Log.d("body",body);
                   // fileWriter.write(body);
                    WeatherEntity weatherEntity;
                    weatherEntity = gson.fromJson(body, WeatherEntity.class);
                    WeatherData(weatherEntity);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void WeatherData(WeatherEntity weatherEntity) {
        tv_location.setText(weatherEntity.getName());
        String temp = String.format("%.1f", (weatherEntity.getMain().getTemp() - 273));
        tv_temperature.setText(temp + " °C");

    }

}




