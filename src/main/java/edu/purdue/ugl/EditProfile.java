package edu.purdue.ugl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class EditProfile extends Activity {
	
	User userInfo;
	TextView nameT;
	TextView phoneT;
	TextView emailT;
	TextView nicknameT;
	TextView descriptionT;
	ImageView profileView;
	
	boolean firstNameChanged = false;
	boolean lastNameChanged = false;
	boolean phoneChange = false;
	boolean emailChanged = false;
	boolean nicknameChanged = false;
	boolean descChange = false;
	
	
	List<NameValuePair> params = new ArrayList<NameValuePair>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        
        nameT = new TextView(this);
        nameT = (TextView)findViewById(R.id.editNameText);
        nameT.setText(User.firstName + " " + User.lastName);
        
        phoneT = new TextView(this);
        phoneT=(TextView)findViewById(R.id.editPhoneText);
        phoneT.setText(User.phoneNumber);
        phoneT.addTextChangedListener(new GenericTextWatcher(phoneT));
        
        emailT = new TextView(this);
        emailT=(TextView)findViewById(R.id.editEmailText);
        emailT.setText(User.email);
        
        nicknameT = new TextView(this);
        nicknameT = (TextView)findViewById(R.id.editNicknameText);
        nicknameT.setText(User.nickname);
        
        descriptionT = new TextView(this);
        descriptionT = (TextView)findViewById(R.id.editDescriptionText);
        descriptionT.setText(User.description);
        
        
    }
    
    private class GenericTextWatcher implements TextWatcher {
    	
    	private View view;
    	private GenericTextWatcher(View view){
    		this.view = view;
    	}

		@Override
		public void afterTextChanged(Editable text) {
			String newText = text.toString();
			switch(view.getId()){
			
			case R.id.editNameText:
				if(newText.contains(" ")){
					String[] str_arr = newText.split(" ");
					User.firstName = str_arr[0];
					User.lastName = str_arr[1];
					firstNameChanged = true;
					lastNameChanged = true;
				}
				else {
					User.firstName = newText;
					User.lastName = "";
					firstNameChanged = true;
				}
				break;
			case R.id.editPhoneText:
				User.phoneNumber = newText;
				phoneChange = true;
				break;
			case R.id.editDescriptionText:
				User.description = newText;
				descChange = true;
				break;
			case R.id.editNicknameText:
				User.nickname = newText;
				break;
			case R.id.editEmailText:
				User.email = newText;
				emailChanged = true;
				break;				
			}
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			// DO nothing because this isnt used
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			
			// Do nothing because this isnt used
		}
    	
    };
    
    
    private void updateList(){
		if(firstNameChanged == true){
			params.add(new BasicNameValuePair("first_name",User.firstName));
		}
		if(lastNameChanged == true) {
			params.add(new BasicNameValuePair("last_name",User.lastName));
		}
    	if(phoneChange == true){
			params.add(new BasicNameValuePair("phone",User.phoneNumber));
    	}
    	if(emailChanged == true){
			params.add(new BasicNameValuePair("email",User.email));
    	}
    	if(descChange == true) {
			params.add(new BasicNameValuePair("phone", User.description));
    	}
    	if(nicknameChanged == true){
    		params.add(new BasicNameValuePair("nickname",User.nickname));
    	}
    	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.doneeditprofile) {
        	updateList();
        	new JSON_Parser(this,params, getApplicationContext(),"POST").execute("http://ugl.sige.us/api/user/edit");
            finish();
            return true;
        }else if(id == R.id.canceleditprofile){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
