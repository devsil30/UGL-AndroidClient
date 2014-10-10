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

public class ViewProfile extends Activity {
	
	User userInfo;
	TextView phoneT;
	TextView emailT;
	TextView nicknameT;
	TextView descriptionT;
	ImageView profileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        userInfo = (User)getApplicationContext();
        
        setTitle(User.firstName +" " + User.lastName);
        profileView = new ImageView(this);
        
        if(User.avatarURL != null){
        	new DownloadImageTask((ImageView) findViewById(R.id.profileImage)).execute(User.avatarURL);
        }
        else{
        	profileView = (ImageView)findViewById(R.id.profileImage);
        	profileView.setImageResource(R.drawable.profilepicture);
        }
        phoneT = new TextView(this);
        phoneT=(TextView)findViewById(R.id.phoneText);
        phoneT.setText(User.phoneNumber);
        
        emailT = new TextView(this);
        emailT=(TextView)findViewById(R.id.emailText);
        emailT.setText(User.email);
        
        nicknameT = new TextView(this);
        nicknameT = (TextView)findViewById(R.id.nicknameText);
        nicknameT.setText(User.nickname);
        
        descriptionT = new TextView(this);
        descriptionT = (TextView)findViewById(R.id.descriptionText);
        descriptionT.setText(User.description);
        
        
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
        getMenuInflater().inflate(R.menu.view_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.editprofile){
            Edit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void Edit(){
        Intent i = new Intent(ViewProfile.this,EditProfile.class);
        startActivity(i);
    }
}
