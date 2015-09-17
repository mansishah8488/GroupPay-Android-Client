package ours.team20.com.groupay.drawer;

/**
 * Created by Ken on 4/28/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ours.team20.com.groupay.R;

import java.util.ArrayList;


public class NavigationDrawerListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<NavigationDrawerItem> navigationDrawerItems;

    public NavigationDrawerListAdapter(Context context, ArrayList<NavigationDrawerItem> navigationDrawerItems){
        this.context = context;
        this.navigationDrawerItems = navigationDrawerItems;
    }

    public NavigationDrawerListAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return navigationDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navigationDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater minflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = minflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        imgIcon.setImageDrawable(navigationDrawerItems.get(position).getIcon());
        txtTitle.setText(navigationDrawerItems.get(position).getTitle());

        return convertView;
    }
}


