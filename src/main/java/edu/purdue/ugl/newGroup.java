package edu.purdue.ugl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class newGroup extends Activity {
	
	EditText newGroupName;
	EditText groupDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        newGroupName = new EditText(this);
        newGroupName = (EditText) findViewById(R.id.newGroupNameText);
        
        groupDescription = new EditText(this);
        groupDescription = (EditText) findViewById(R.id.newGroupDescrption);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.newgroup) {
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
        	params.add(new BasicNameValuePair("alias", newGroupName.getText().toString()));
        	params.add(new BasicNameValuePair("description", groupDescription.getText().toString()));
        	params.add(new BasicNameValuePair("tags", "null"));
        	params.add(new BasicNameValuePair("status","1"));
        	new JSON_Parser(this,params,getApplicationContext(),"POST").execute("http://ugl.sige.us/api/group/create");
            //Toast.makeText(getApplicationContext(),"todo",Toast.LENGTH_SHORT).show();
        	finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
