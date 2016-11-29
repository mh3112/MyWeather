package minhhang.mta.myweather.data;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateParsingException;

import minhhang.mta.myweather.MainActivity;

import static java.net.Proxy.Type.HTTP;

/**
 * Created by Minh Hang on 11/11/2016.
 */

public interface JSONparser {
  /*  class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String str = getXMLfromURL(params[0]);
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
        }
    }

    private String getXMLfromURL(String urlString) {
        String xml = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlString);

            HttpResponse httpResponse = httpClient.Excute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity, HTTP.UTF_8);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }
*/
}
