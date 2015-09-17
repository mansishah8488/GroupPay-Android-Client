package ours.team20.com.groupay.groupfragments;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ours.team20.com.groupay.MyExecutor;
import ours.team20.com.groupay.R;
import ours.team20.com.groupay.RESTApi.NodejsCall;
import ours.team20.com.groupay.listadapter.Item;
import ours.team20.com.groupay.listadapter.ItemAdapter;
import ours.team20.com.groupay.models.User;
import ours.team20.com.groupay.singletons.UserSingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment implements View.OnClickListener {

    private ListView eventList;
    private Button createEvent;
    private ArrayList<String> eventArrayList;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> eventIds;
    private ItemAdapter adapter;
    private ArrayList<Item> items;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);

        eventList = (ListView) v.findViewById(R.id.event_list);
        createEvent = (Button) v.findViewById(R.id.create_event);
        createEvent.setOnClickListener(this);
        new FetchEvents().executeOnExecutor(MyExecutor.getExecutor());

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventid = eventIds.get(position);

            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_event:{
                createDialog();
            }
        }
    }

    public void createDialog(){

        final Dialog createEventDialog = new Dialog(getActivity());

        createEventDialog.setContentView(R.layout.fragment_create_event_dialog);
        createEventDialog.setTitle("Create an event..");

        createEventDialog.show();

        Button createButton = (Button) createEventDialog.findViewById(R.id.confirm);
        Button cancelButton = (Button) createEventDialog.findViewById(R.id.cancel);
        final EditText event_name = (EditText) createEventDialog.findViewById(R.id.event_name);
        final EditText event_description = (EditText) createEventDialog.findViewById(R.id.event_description);
        final EditText event_date = (EditText) createEventDialog.findViewById(R.id.event_date);
        final EditText event_cost = (EditText) createEventDialog.findViewById(R.id.event_cost);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(event_name.getText().equals("") || event_date.getText().equals("")){
                    Toast.makeText(getActivity(),
                            "Please enter the name and/or date", Toast.LENGTH_SHORT).show();
                }

                else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name", event_name.getText());
                        jsonObject.put("event_cost", Long.parseLong(event_cost.getText().toString()));
                        jsonObject.put("date", event_date.getText());
                        jsonObject.put("group", getActivity().getIntent().getStringExtra("groupid"));
                        jsonObject.put("description", event_description.getText());
                        //new CreateEvent().execute(jsonObject);
                        new CreateEvent().executeOnExecutor(MyExecutor.getExecutor(), jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),
                                "Event could not be created", Toast.LENGTH_SHORT).show();
                    }
                    createEventDialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventDialog.dismiss();
            }
        });
    }

    private class CreateEvent extends AsyncTask<JSONObject, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            JSONObject jsonObject = params[0];
            try {
                User user = UserSingleton.getCurrentUser();
                String url = "/user/" + user.getId() + "/group/"
                        + getActivity().getIntent().getStringExtra("groupid")
                        + "/events";

                JSONObject resJsonObject = NodejsCall.post(url, jsonObject);

                return resJsonObject;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "Event could not be created", Toast.LENGTH_SHORT).show();
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
                    Item item = new Item(jsonObject.getJSONObject("data").getString("name"),
                            "Event cost : " + Long.toString(jsonObject.getJSONObject("data").getLong("event_cost")));

                    if(adapter == null){
                        items = new ArrayList<Item>();
                        items.add(item);
                        adapter = new ItemAdapter(getActivity(), items);
                        eventList.setAdapter(adapter);
                    }
                    else{
                        adapter.add(item);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Event could not be created", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class FetchEvents extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... params) {
            String groupid = getActivity().getIntent().getStringExtra("groupid");
            String url = "/user/" + UserSingleton.getCurrentUser().getId() + "/group/"
                    + groupid + "/events";

            JSONArray eventArray = NodejsCall.getArray(url);
            return eventArray;
        }

        @Override
        public void onPostExecute(JSONArray eventArray){
            eventArrayList = new ArrayList<>();
            eventIds = new ArrayList<>();
            for(int i=0;i<eventArray.length();i++){
                try {
                    eventArrayList.add(eventArray.getJSONObject(i).getString("name"));
                    eventIds.add(eventArray.getJSONObject(i).getString("eventid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(eventArrayList.isEmpty()){
                listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, eventArrayList);
                eventList.setAdapter(listAdapter);
            }
        }
    }
}
