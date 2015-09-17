package ours.team20.com.groupay;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ours.team20.com.groupay.models.User;
import ours.team20.com.groupay.singletons.UserSingleton;

public class MainActivity extends ActionBarActivity{
    CallbackManager callbackManager;
    AccessToken accessToken;
    String userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        // Chk if user already logged In.
        if(savedInstanceState == null){
            Log.d("USER", "FirstLaunch");
        } else {
            String myUser = savedInstanceState.getString("UserID");
            //Log.d("USER",myUser);
            ApiCall();
        }

        setContentView(R.layout.activity_main);

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                accessToken = loginResult.getAccessToken();
                String token = accessToken.getToken();
                Log.d("success Name :", token);
                ApiCall();
            }

            @Override
            public void onCancel() {
                Log.d("canceled:" , "canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("error:" , "error");
            }
        });

//        if(loginButton.getText().equals("Log out")){
//            ApiCall();
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("userID", userId);
    }

    public void ApiCall() {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        String data = response.getRawResponse();
                        try {
                            JSONObject obj = new JSONObject(data);
//                            userId = obj.getString("id");
//                            String name = obj.getString("name");
//                            String URL = obj.getJSONObject("picture").getJSONObject("data").getString("url");
//                            Log.d("URL",URL);
//                            intent = new Intent(MainActivity.this, LoggedinActivity.class);
//                            intent.putExtra("UserId", userId);
//                            intent.putExtra("UserName", name);
//                            intent.putExtra("Profile",URL);
                            new LoginRest().executeOnExecutor(MyExecutor.getExecutor(), obj);
                            //startActivity(intent);
//                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields","id, name, picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //To make rest request for logging into the app
    private class LoginRest extends AsyncTask<JSONObject, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPostReq = new HttpPost("http://10.189.174.58:3000/user/login");

            JSONObject userObj = params[0];
            String name = null;
            String URL = null;
            try {
                userId = userObj.getString("id");
                name = userObj.getString("name");
                URL = userObj.getJSONObject("picture").getJSONObject("data").getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(userId == null || name == null || URL == null){
                return null;
            }
            User user = new User(userId, name, URL);
            JSONObject jsonObject = null;
            try {
                StringEntity se = new StringEntity(user.toJSONObject().toString());
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httpPostReq.setEntity(se);
                HttpResponse httpResponse = httpClient.execute(httpPostReq);
                String responseText = null;
                responseText = EntityUtils.toString(httpResponse.getEntity());
                jsonObject = new JSONObject(responseText);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,
                        "There is some problem connecting to the server", Toast.LENGTH_LONG).show();

            }

            return jsonObject;
        }

        @Override
        public void onPostExecute(JSONObject userObj){
            User user = null;
            if(userObj != null)
                try {
                    user = new User(userObj.getJSONObject("data").getString("userid"),
                            userObj.getJSONObject("data").getString("name"),
                            userObj.getJSONObject("data").getString("URL"));
                    UserSingleton userSingleton = new UserSingleton(user);
                    Intent goToLoggedinIntent = new Intent(MainActivity.this, LoggedinActivity.class);
                    startActivity(goToLoggedinIntent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
    }


}
