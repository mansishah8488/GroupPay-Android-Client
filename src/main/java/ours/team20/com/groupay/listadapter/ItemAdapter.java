package ours.team20.com.groupay.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ours.team20.com.groupay.R;

/**
 * Created by Ken on 4/26/2015.
 */
public class ItemAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<Item> itemsArrayList;

    public ItemAdapter(Context context, ArrayList<Item> itemsArrayList) {
        super(context, R.layout.row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        //3. Get the two text view from the rowView
        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        TextView detailView = (TextView) rowView.findViewById(R.id.detail);

        // 4. Set the text for textView
        nameView.setText(itemsArrayList.get(position).getName());
        detailView.setText(itemsArrayList.get(position).getDetail());

        // 5. return rowView
        return rowView;
    }

}