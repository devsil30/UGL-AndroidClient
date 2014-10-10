package edu.purdue.ugl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import edu.purdue.ugl.User.Groups;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainPage extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    
    private int groupNum = 0;
    User userInfo;

    private String[] tabs = {"Stickyboard","Item Bag","Wallet"};
    
    JSONObject jObj;
    String success = null;
    String timestamp = null;
    int userID = 0;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mContext = getApplicationContext();
        Intent intent = getIntent();
        String user = intent.getStringExtra("JSONObject");
        
        userInfo =(User)mContext.getApplicationContext();
        try{
        	jObj = new JSONObject(user); 
        	success = jObj.getString("status");
        	timestamp = jObj.getString("expiration");
        	
        	JSONObject jData = jObj.getJSONObject("data");
        	
        	/* get the user ID and set it in the User class if the use is not currently saved */
        	if(userInfo.isSaved == false){
        		userID = jData.getInt("user_id");
        		String url = "http://ugl.sige.us/api/user/info/"+userID;
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
        		new JSON_Parser(this,params, mContext, "POST").execute(url);
        	} 
        	else{
        		Toast.makeText(mContext, "Unable to load user data", Toast.LENGTH_SHORT).show();
        	}
        	
        }catch (JSONException e) {
        	e.printStackTrace();
        }
        
        if(success != null){
        	Toast.makeText(getApplicationContext(), "Welcome to UGLi!", Toast.LENGTH_SHORT).show();
        	//System.out.println(userInfo.firstName);
        }
        else{
        	Toast.makeText(getApplicationContext(), "No User Data", Toast.LENGTH_SHORT).show();
        }

        viewPager = (ViewPager) findViewById(R.id.tabs);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for(String tab_name : tabs){
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                .setTabListener(this));

        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
               
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Settings();
            return true;
        }else if(id == R.id.viewedit){
            View();
            return true;
        }else if(id == R.id.groupmembers){
            GroupMembers();
            return true;
        }else if(id == R.id.vieweditgroup){
        	String[] groups = new String[User.groupCount];
        	for(int i = 0; i < groups.length;i++){
        		groups[i] = User.groupsList.get(i).alias;
        	}
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setTitle("Select a Group");
        	builder.setItems(groups, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Intent i = new Intent(MainPage.this,GroupProfile.class);
				    i.putExtra("intGroup", which);
				    startActivity(i);
				}
			} );
        	 AlertDialog groupOptions = builder.create();
        	 groupOptions.show();
             return true;
        }else if(id == R.id.sync){
            Toast.makeText(getApplicationContext(),"Syncing Data...",Toast.LENGTH_LONG).show();
            System.out.println(User.id);
            List<NameValuePair> empty = new ArrayList<NameValuePair>();
            new JSON_Parser(this,empty,getApplicationContext(),"POST").execute("http://ugl.sige.us/api/user/listgroup/"+User.id);
            return true;
        }else if(id == R.id.leave_group){
            Toast.makeText(getApplicationContext(),"todo",Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.newgroup){
            newGroup();
            return true;
        }
            return super.onOptionsItemSelected(item);
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }
    
    @Override 
    protected void onResume() {
    	super.onResume();
    	userInfo = ((User) getApplicationContext());
    }
    private void View(){
        Intent i = new Intent(MainPage.this,ViewProfile.class);
        startActivity(i);
    }
    private void Settings(){
        Intent i = new Intent(MainPage.this,Settings.class);
        startActivity(i);
    }
    private void GroupMembers(){
        Intent i = new Intent(MainPage.this,GroupMembers.class);
        startActivity(i);
    }
    private void ViewEditGroupProfile(int which){
        Intent i = new Intent(MainPage.this,GroupProfile.class);
        i.putExtra("intGroup", which);
        startActivity(i);
    }
    private void newGroup(){
        Intent i = new Intent(MainPage.this,newGroup.class);
        startActivity(i);
    }

    
}
