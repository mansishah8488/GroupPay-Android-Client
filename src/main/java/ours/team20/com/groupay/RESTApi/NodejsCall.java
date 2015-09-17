package ours.team20.com.groupay.RESTApi;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ours.team20.com.groupay.models.User;

/**
 * Created by Ken on 4/24/2015.
 */
public class NodejsCall {
    public static String NODEJS = "http://10.189.174.58:3000";

    public static JSONObject get(String url){
        JSONObject resJsonObject = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGetReq = new HttpGet(NODEJS + url);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGetReq);
            String responseText = EntityUtils.toString(httpResponse.getEntity());
            resJsonObject = new JSONObject(responseText);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJsonObject;
    }

    public static JSONObject post(String url, JSONObject jsonObject){
        JSONObject resJsonObject = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPostReq = new HttpPost(NODEJS + url);
        //User user = new User(1, "Sumit", "svalecha91@gmail.com");

        try {
            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentType("application/json;charset=UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            httpPostReq.setEntity(se);
            HttpResponse httpResponse = httpClient.execute(httpPostReq);
            String responseText = EntityUtils.toString(httpResponse.getEntity());
            resJsonObject = new JSONObject(responseText);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJsonObject;
    }

    public static JSONArray getArray(String url){
        JSONArray resJsonArray = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGetReq = new HttpGet(NODEJS + url);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGetReq);
            String responseText = EntityUtils.toString(httpResponse.getEntity());
            JSONObject jsonObject = new JSONObject(responseText);

            resJsonArray = jsonObject.getJSONArray("data");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJsonArray;
    }

}
