package ours.team20.com.groupay;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.AccessToken;

import java.io.IOException;
import java.util.ArrayList;

import ours.team20.com.groupay.drawer.NavigationDrawerItem;
import ours.team20.com.groupay.drawer.NavigationDrawerListAdapter;
import ours.team20.com.groupay.imagedownloader.DownloadImage;


public class LoggedinActivity extends ActionBarActivity implements View.OnClickListener{
    private Fragment fragment = null;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ListView drawerList;

    //Navigation Drawer Title
    private CharSequence drawerTitle;

    private String[] menuTitles;
    private TypedArray menuIcons;

    private ArrayList<NavigationDrawerItem> navDrawerItems;
    private NavigationDrawerListAdapter naviDrawerListAdapter;
    private Button createGroupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);

        drawerTitle = getTitle();

        menuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        menuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        View header = getLayoutInflater().inflate(R.layout.header, null);


        drawerList = (ListView) findViewById(R.id.list_slider_menu);
        drawerList.addHeaderView(header);
        navDrawerItems = new ArrayList<NavigationDrawerItem>();

        for(int i = 0; i < menuTitles.length; i++){
            navDrawerItems.add(new NavigationDrawerItem(menuTitles[i], menuIcons.getDrawable(i)));
        }

        drawerList.setOnItemClickListener(new SlideMenuClickListener());


        naviDrawerListAdapter = new NavigationDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        drawerList.setAdapter(naviDrawerListAdapter);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                new Toolbar(LoggedinActivity.this),
                R.string.app_name,
                R.string.app_name)
        {
            public void onDrawerClosed(View v){
                getSupportActionBar().setTitle(drawerTitle);


                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                getSupportActionBar().setTitle(drawerTitle);


                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().hide();
        displayView(0);

    }

    @Override
    public void onClick(View v) {

    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loggedin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        AccessToken.setCurrentAccessToken(null);
        Intent intent = new Intent(LoggedinActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setProfileImage(String src) throws IOException {
        ImageView iv = (ImageView)findViewById(R.id.image_profile);
        new DownloadImage(iv).execute(src);
    }

    private void displayView(int position){

        fragment = null;
        switch (position){
            case 0: {

                fragment = new MyGroupFragment2();
                break;
            }
            case 1: {
                fragment = new AllGroupFragment();
                break;
            }
            case 2: {
                fragment = new NotificationFragment();
                break;
            }
            default:{
                fragment = new MyGroupFragment2();
                break;
            }
        }

        attachFragment(position);

    }


    private void attachFragment(int position){

        if(fragment != null){


            FragmentManager fragmentManager = getFragmentManager();


            fragmentManager.beginTransaction().replace(R.id.loggedin_fragments, fragment).commit();


//            drawerList.setItemChecked(position, true);
//            drawerList.setSelection(position);
//            setTitle(menuTitles[position]);
//            drawerLayout.closeDrawer(drawerList);
        }
        else{

            Log.e("EnteredActivity", "Error in creating fragment");
        }
    }

    @Override
    public void onBackPressed(){
        //To do nothing
    }
}
