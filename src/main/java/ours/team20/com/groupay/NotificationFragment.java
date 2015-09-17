package ours.team20.com.groupay;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.util.ArrayList;

import ours.team20.com.groupay.RESTApi.NodejsCall;
import ours.team20.com.groupay.listadapter.Item;
import ours.team20.com.groupay.listadapter.ItemAdapter;
import ours.team20.com.groupay.singletons.UserSingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private ArrayList<String> notificationIds;
    private ArrayList<Item> notificationDetailList;
    private ListView notificationList;
    private ItemAdapter adapter;
    private boolean response = false;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_notification, container, false);

        notificationList = (ListView) v.findViewById(R.id.notification_list);
        new FetchNotifications().executeOnExecutor(MyExecutor.getExecutor());
        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createDialog(view, notificationIds.get(position));
            }
        });

        return v;
    }

    public void createDialog(View v, String id) {
        final String idmain = id;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        //set title
        alertDialogBuilder.setTitle("User request");

        //
        alertDialogBuilder.setMessage("Allow the user?")
                .setCancelable(false)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        response = true;
                        new RespondPermission().execute(idmain);
                        new FetchNotifications().execute();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        response = false;
                        new RespondPermission().execute(idmain);
                        new FetchNotifications().execute();
                        dialog.dismiss();
                    }
                });

        alertDialogBuilder.show();
    }


    private class FetchNotifications extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... params) {
            String url = "/admin/" + UserSingleton.getCurrentUser().getId() + "/requests";

            JSONArray jsonArray = NodejsCall.getArray(url);

            return jsonArray;
        }

        @Override
        public void onPostExecute(JSONArray notificationArray){
            notificationIds = new ArrayList<>();
            notificationDetailList = new ArrayList<>();
            for(int i=0;i<notificationArray.length();i++){
                try {

                    String groupdId = notificationArray.getJSONObject(i).getString("groupid");
                    String groupName = notificationArray.getJSONObject(i).getString("group");
                    String groupUser = notificationArray.getJSONObject(i).getString("user");
                    notificationDetailList.add(new Item(groupName, groupUser));
                    notificationIds.add(groupdId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(!notificationDetailList.isEmpty()){
                adapter = new ItemAdapter(getActivity(), notificationDetailList);
                notificationList.setAdapter(adapter);
            }
        }
    }

    private class RespondPermission extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
            String id = params[0];
            String url = "/request/" + id;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("response", response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject responseObj = NodejsCall.post(url, jsonObject);
            return responseObj;
        }

        @Override
        public void onPostExecute(JSONObject jsonObject){

        }
    }
}
