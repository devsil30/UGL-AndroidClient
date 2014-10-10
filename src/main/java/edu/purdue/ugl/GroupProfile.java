package edu.purdue.ugl;

import java.io.InputStream;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupProfile extends Activity {
	
	TextView groupName;
	TextView groupDesc;
	TextView groupStatus;
	TextView groupNumber;
	TextView groupMembers;
	ImageView groupImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        int index = extras.getInt("intGroup");
        
        groupImage = new ImageView(this);
        
        if(!User.groupsList.get(index).groupAvatar.equals("")){
        	new DownloadImageTask((ImageView) findViewById(R.id.groupImageView)).execute(User.groupsList.get(index).groupAvatar);
        }
        else{
        	groupImage = (ImageView)findViewById(R.id.groupImageView);
        	groupImage.setImageResource(R.drawable.profilepicture);
        }
        
        groupName = new TextView(this);
        groupName = (TextView) findViewById(R.id.groupNameText);
        groupName.setText(User.groupsList.get(index).alias);
        groupDesc = new TextView(this);
        groupDesc = (TextView) findViewById(R.id.groupDescriptionText);
        groupDesc.setText(User.groupsList.get(index).groupDescription);
        groupStatus = new TextView(this);
        groupStatus = (TextView) findViewById(R.id.groupStatusText);
        if(User.groupsList.get(index).status == 0){
        	groupStatus.setText("deleted: " + User.groupsList.get(index).status);
        }
        else if(User.groupsList.get(index).status == 1){
        	groupStatus.setText("Inactive: " + User.groupsList.get(index).status);
    	}
        else if(User.groupsList.get(index).status == 2){
        	groupStatus.setText("Private: " + User.groupsList.get(index).status);
        }
        else if(User.groupsList.get(index).status == 3){
        	groupStatus.setText("Public: " + User.groupsList.get(index).status);
        }
        groupMembers = new TextView(this);
        groupMembers = (TextView) findViewById(R.id.groupMemberList);
        //groupMembers.setText(""+User.groupsList.get(0).groupUsers.get(0));
        groupNumber = new TextView(this);
        groupNumber = (TextView) findViewById(R.id.groupMemberNumber);
        groupNumber.setText("Number of Members:  " + User.groupsList.get(index).num_of_users);

    }
    
    private class DownloadImageTask extends AsyncTask<String,Void, Bitmap>{
    	ImageView bmImage;
    	
    	public DownloadImageTask(ImageView bmImage){
    		this.bmImage = bmImage;
    	}

		@Override
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try{
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			}
			catch (Exception e){
				Log.e("Error",e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}
		
		protected void onPostExecute(Bitmap result){
			bmImage.setImageBitmap(result);
		}
    }
    	


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.editgroupprofile) {
            Edit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void Edit(){
        Intent i = new Intent(GroupProfile.this,EditGroupProfile.class);
        startActivity(i);
    }
}
