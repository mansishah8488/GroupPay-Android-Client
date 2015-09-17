package ours.team20.com.groupay;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.DialogInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ours.team20.com.groupay.RESTApi.NodejsCall;
import ours.team20.com.groupay.listadapter.Item;
import ours.team20.com.groupay.listadapter.ItemAdapter;
import ours.team20.com.groupay.singletons.UserSingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllGroupFragment extends Fragment {

    private ListView allGroupList;
    private ArrayList<String> groupids;
    private ArrayList<String> groupnames;
    private ArrayList<Item> items;
    ItemAdapter adapter;
    public AllGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_group, container, false);

        allGroupList = (ListView) v.findViewById(R.id.all_group_list);
        new AllGroupFetcher().execute();
        allGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createDialog(view, items.get(position).getDetail());
            }
        });


        return v;
    }

    public void createDialog(View v, final String groupid) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        //set title
        alertDialogBuilder.setTitle("Join group");

        //
        alertDialogBuilder.setMessage("Click yes to join!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SendJoinRequest().execute(groupid);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.show();
    }

    private class AllGroupFetcher extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... params) {
            String url = "/user/" + UserSingleton.getCurrentUser().getId() + "/allgroup";

            JSONArray groupArray = NodejsCall.getArray(url);

            return groupArray;

        }

        @Override
        public void onPostExecute(JSONArray groupArray){
            items = new ArrayList<>();
            try {
                for (int i = 0; i < groupArray.length(); i++) {
                    String groupid = groupArray.getJSONObject(i).getString("groupid");
                    String groupname = groupArray.getJSONObject(i).getString("name");

                    items.add(new Item(groupname, groupid));
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }


            if(!items.isEmpty()){
                adapter = new ItemAdapter(getActivity(), items);
                allGroupList.setAdapter(adapter);
            }
        }
    }

    private class SendJoinRequest extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
            String groupid = params[0];

            String url = "/user/" + UserSingleton.getCurrentUser().getId()
                    + "/join/" + groupid;

            JSONObject userObj = new JSONObject();
            try {
                userObj.put("name", UserSingleton.getCurrentUser().getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonObject = NodejsCall.post(url, userObj);

            return jsonObject;
        }

        @Override
        public void onPostExecute(JSONObject jsonObject){

        }
    }

}
