package ours.team20.com.groupay.groupfragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import ours.team20.com.groupay.R;
import ours.team20.com.groupay.RESTApi.NodejsCall;
import ours.team20.com.groupay.singletons.UserSingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    private ListView paymentHistoryList;
    private TextView moneypoolText;
    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        moneypoolText = (TextView) v.findViewById(R.id.moneypool_amount);
        paymentHistoryList = (ListView) v.findViewById(R.id.payment_history);
        String groupid = getActivity().getIntent().getStringExtra("groupid");
        String groupname = getActivity().getIntent().getStringExtra("groupname");

        moneypoolText.setText(groupname);
        new PaymentFetcher().execute();
        return v;
    }

    private class PaymentFetcher extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... params) {
            String userid = UserSingleton.getCurrentUser().getId();
            String url = "/user/" + userid + "/payments";

            JSONArray paymentArray= NodejsCall.getArray(url);

            return paymentArray;
        }

        @Override
        public void onPostExecute(JSONArray jsonArray){
            ArrayList<String> paymentList = new ArrayList<>();
            ArrayList<String> paymentDate = new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                try {
                    paymentList.add(jsonArray.getJSONObject(i).getString("payment"));
                    paymentDate.add(jsonArray.getJSONObject(i).getString("eventid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(paymentList.isEmpty()){
                ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, paymentList);

                paymentHistoryList.setAdapter(listAdapter);
            }
        }
    }

}
