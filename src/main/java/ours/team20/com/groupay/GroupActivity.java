package ours.team20.com.groupay;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ours.team20.com.groupay.groupfragments.EventsFragment;
import ours.team20.com.groupay.groupfragments.PaymentFragment;
import ours.team20.com.groupay.groupfragments.PeopleFragment;


public class GroupActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(getIntent().getStringExtra("groupname"));

        ActionBar.Tab peopleTab = actionBar.newTab().setText("People");//.setIcon();
        ActionBar.Tab paymentTab = actionBar.newTab().setText("Payments");
        ActionBar.Tab eventsTab = actionBar.newTab().setText("Events");

        Fragment peopleFragment = new PeopleFragment();
        Fragment paymentFragment = new PaymentFragment();
        Fragment eventsFragment = new EventsFragment();

        peopleTab.setTabListener(new MyTabListener(peopleFragment));
        paymentTab.setTabListener(new MyTabListener(paymentFragment));
        eventsTab.setTabListener(new MyTabListener(eventsFragment));

        actionBar.addTab(peopleTab);
        actionBar.addTab(paymentTab);
        actionBar.addTab(eventsTab);
    }

    class MyTabListener implements ActionBar.TabListener{

        public Fragment fragment;
        public MyTabListener(Fragment fragment){
            this.fragment = fragment;
        }
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            //TODO
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            //TODO
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group, menu);
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
}
