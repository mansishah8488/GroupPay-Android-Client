package ours.team20.com.groupay;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ours.team20.com.groupay.RESTApi.NodejsCall;
import ours.team20.com.groupay.listadapter.Item;
import ours.team20.com.groupay.listadapter.ItemAdapter;
import ours.team20.com.groupay.models.User;
import ours.team20.com.groupay.singletons.UserSingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupFragment2 extends Fragment implements View.OnClickListener{
    private User user;
    private ArrayList<String> groupids;
    private ArrayList<Item> items;
    private ListView myGroupList;
    private Button createGroupButton;
    private ItemAdapter adapter = null;
    private View vForCreate;
    public MyGroupFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_group2, container, false);
        vForCreate = inflater.inflate(R.layout.fragment_create_group_dialog, container, false);
        createGroupButton = (Button) v.findViewById(R.id.create_group);
        createGroupButton.setOnClickListener(this);
        myGroupList = (ListView) v.findViewById(R.id.group_list);
        ItemListenerClass itemListenerClass = new ItemListenerClass();
        myGroupList.setOnItemClickListener(itemListenerClass);

        new GetMyGroupsTask().execute();

        return v;
    }

    private class ItemListenerClass implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(), "position:" + user.getGroups().get(position)
                    , Toast.LENGTH_SHORT).show();

            Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
            String groupname = ((Item)myGroupList.getItemAtPosition(position)).getName();
            groupIntent.putExtra("groupid", user.getGroups().get(position));
            groupIntent.putExtra("groupname", groupname);
            startActivity(groupIntent);
        }
    }



    private class GetMyGroupsTask extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... params) {
            user = UserSingleton.getCurrentUser();
            String url = "/user/"+ user.getId() +"/groups";
            JSONArray jsonArray = NodejsCall.getArray(url);

            return jsonArray;
        }

        @Override
        public void onPostExecute(JSONArray groups){
            groupids = new ArrayList<>();
            items = new ArrayList<Item>();
            try {
                for (int i = 0; i < groups.length(); i++) {
                    String groupid = groups.getJSONObject(i).getString("groupid");

                    items.add(new Item(groups.getJSONObject(i).getString("name"),
                            "Money pool : " + Long.toString(groups.getJSONObject(i).getLong("moneypool"))));

                    if(!user.getGroups().contains(groupid)) {
                        groupids.add(groupid);
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

            user.setGroups(groupids);
            if(!items.isEmpty()){
                adapter = new ItemAdapter(getActivity(), items);
                myGroupList.setAdapter(adapter);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.create_group: {

                createDialog(vForCreate);
                break;
            }
        }
    }


    public void createDialog(View v){
        final Dialog createGroupDialog = new Dialog(getActivity());

        createGroupDialog.setContentView(R.layout.fragment_create_group_dialog);
        createGroupDialog.setTitle("Create a group..");

        createGroupDialog.show();

        Button createButton = (Button) createGroupDialog.findViewById(R.id.confirm);
        Button cancelButton = (Button) createGroupDialog.findViewById(R.id.cancel);
        final EditText group_name = (EditText) createGroupDialog.findViewById(R.id.group_name);
        final EditText frequency_amount = (EditText) createGroupDialog.findViewById(R.id.frequency_amount);
        final Spinner frequency_spinner = (Spinner) createGroupDialog.findViewById(R.id.frequency_spinner);
        final Spinner frequency_type_spinner = (Spinner) createGroupDialog.findViewById(R.id.frequency_type_spinner);

        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.frequency, R.layout.support_simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> frequencyTypeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.frequency_type, R.layout.support_simple_spinner_dropdown_item);

        frequencyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        frequencyTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        frequency_spinner.setAdapter(frequencyAdapter);
        frequency_type_spinner.setAdapter(frequencyTypeAdapter);



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group_name.getText().equals("") || frequency_amount.getText().equals("")){
                    Toast.makeText(getActivity(),
                            "Please enter the name and/or amount", Toast.LENGTH_SHORT).show();
                }

                else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name", group_name.getText());
                        jsonObject.put("frequency_amount", Long.parseLong(frequency_amount.getText().toString()));
                        jsonObject.put("frequency", Long.parseLong(frequency_spinner.getSelectedItem().toString()));
                        jsonObject.put("frequency_type", frequency_type_spinner.getSelectedItem().toString());
                        new CreateGroup().execute(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),
                                "Group could not be created", Toast.LENGTH_SHORT).show();
                    }
                    createGroupDialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroupDialog.dismiss();
            }
        });
    }

    private class CreateGroup extends AsyncTask<JSONObject, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            JSONObject jsonObject = params[0];
            try {
                jsonObject.put("created_at", new Date().toString());
                jsonObject.put("admin", user.getId());

                String url = "/user/" + user.getId() + "/groups";

                JSONObject resJsonObject = NodejsCall.post(url, jsonObject);

                return resJsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "Group could not be created", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        public void onPostExecute(JSONObject jsonObject){
            if(jsonObject == null){
                Toast.makeText(getActivity(),
                        "There is some problem connecting to the server", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    user.getGroups().add(jsonObject.getJSONObject("data").getString("groupid"));
                    Item item = new Item(jsonObject.getJSONObject("data").getString("name"),
                            "Money pool : " + Long.toString(jsonObject.getJSONObject("data").getLong("moneypool")));
                    if(adapter == null){
                        items = new ArrayList<Item>();
                        items.add(item);
                        adapter = new ItemAdapter(getActivity(), items);
                        myGroupList.setAdapter(adapter);
                    }
                    else{
                        adapter.add(item);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Group could not be created", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
