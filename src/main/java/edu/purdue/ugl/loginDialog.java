package edu.purdue.ugl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class loginDialog extends DialogFragment implements
	ConnectionCallbacks, OnConnectionFailedListener, com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks, com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener {
    Activity mAct;
    
    /* Username Text Box and Password Text Box*/
    EditText userNameText;
    EditText passwordText;
        
    String mScope;
    /* This is a boolean the status of the Sign in Button*/
    private boolean mSignInClicked;
    
    /* Connection result for the Google Sign in Button */
    private ConnectionResult mConnectionResult;
    
    /* Request code used to the invoke sign in user interactions.*/ 
    private static final int RC_SIGN_IN = 0;
    
   /* Client used to interact with Google APIs.*/ 
    private GoogleApiClient mGoogleApiClient;
    
    /* flag indicated that a PendingIntent is in progress preventing us from starting further intents*/
    private boolean mIntentInProgress;
    
    private EncryptHelper encrypt;
    
    /*  The parser/writer for the JSON object writing to the login API of the server*/
    String url = "http://ugl.sige.us/api";
    //JSON_Parser jsonp = new JSON_Parser("http://ugl.sige.us/api/login");
    @Override
    public void onAttach(Activity act){
        super.onAttach(act);
        
        mAct=act;
        
        encrypt = new EncryptHelper();
        
     	mGoogleApiClient = new GoogleApiClient.Builder(mAct)
    	.addConnectionCallbacks(this)
    	.addOnConnectionFailedListener(this)
    	.addApi(Plus.API,null)
    	.addScope(Plus.SCOPE_PLUS_LOGIN)
    	.build();
    	
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.loginw)
                .setItems(R.array.account_types, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if(which==0){
                        	
                        	/* Do user interface stuff */                        	
                            Toast.makeText(getActivity(),"Google",Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater=getActivity().getLayoutInflater();
                            View view = inflater.inflate(R.layout.google_dialog_layout,null);
                            userNameText = (EditText) view.findViewById(R.id.username);
                            passwordText = (EditText) view.findViewById(R.id.password);
                            builder1.setView(view);
                            builder1.setTitle("Login with a Google Account");
                            
                            builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            	@Override
                            	public void onClick(DialogInterface dialog2, int which) {
                            		dialog2.dismiss();
                            	}
                            });
                            
                            //view.findViewById(R.id.sign_in_button);
                            builder1.setPositiveButton(R.id.sign_in_button, new DialogInterface.OnClickListener() {
                            	@Override
                            	public void onClick(DialogInterface dialog2, int which) {
                            		// Do Google Stuff here
                            		String username = userNameText.getText().toString();
                            		///Start Connecting
                            		if(!mGoogleApiClient.isConnecting() && !username.isEmpty()) {
                            			mSignInClicked = true;
                            			resolveSignInError();
                            			/*
                            			try {
											String token = GoogleAuthUtil.getToken(mAct, username, mScope);
										} catch (UserRecoverableAuthException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										} catch (GoogleAuthException e) {
											e.printStackTrace();
										}
										*/
                            	}
                            	}
                            });
                            Dialog dialog1 = builder1.create();
                            dialog1.show();
                        }else if(which==1){
                            Toast.makeText(getActivity(),"Facebook",Toast.LENGTH_SHORT).show();
                        }else if(which==2){
                            AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater=getActivity().getLayoutInflater();
                            View view = inflater.inflate(R.layout.login_dialog,null);
                            builder1.setView(view);
                            userNameText = (EditText) view.findViewById(R.id.username);
                            passwordText = (EditText) view.findViewById(R.id.password);
                            builder1.setTitle("Login with Email Address");
                            builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    dialog1.dismiss();
                                }
                            });
                            builder1.setNeutralButton("Forgot Password",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog1, int which){
                                    dialog1.dismiss();
                                    AlertDialog.Builder builder2=new AlertDialog.Builder(mAct);
                                    builder2.setTitle("Forgot Password");
                                    LayoutInflater inflater1=mAct.getLayoutInflater();
                                    View view1=inflater1.inflate(R.layout.forgot_password, null);
                                    builder2.setView(view1);
                                    Editable usernameBox = userNameText.getText();
                                    String email = usernameBox.toString();
                                    if(!email.isEmpty()){
                                    	url = url+"/resetPassword";
                                    	List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    	params.add(new BasicNameValuePair("email", email));
                                    	Toast.makeText(getActivity(), "Please check your e-mail", Toast.LENGTH_SHORT).show();
                                    	new JSON_Parser(mAct,params,mAct.getApplicationContext(),"POST").execute(url);
                                    	
                                    }
                                    else{
                                    	Toast.makeText(getActivity(), "Please enter your e-mail address", Toast.LENGTH_SHORT).show();
                                    }
                                    

                                    builder2.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(mAct,"Please check your email",Toast.LENGTH_SHORT).show(); //Force Closes. Nullpointer Exception? Probably because of getActivity()
                                        }
                                    });
                                    builder2.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                           dialog.dismiss();
                                        }
                                    });
                                    builder2.show();
                                }
                            });
                            builder1.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    //Toast.makeText(mAct,"Login complete",Toast.LENGTH_SHORT).show(); //Force Closes. Nullpointer Exception? Probably because of getActivity()
                                    dialog1.dismiss();
                                    Context context = mAct.getApplicationContext();
                                    
                                    /*get email and password from text boxes*/
                                    Editable usernameBox = userNameText.getText();
                                    Editable passwordBox = passwordText.getText();
                                    String email = usernameBox.toString();
                                    String password = passwordBox.toString();
                                    
                                    if(password.length() < 6){
                                    	Toast.makeText(mAct, "password must be at least 6 characters long", Toast.LENGTH_LONG).show();
                                    }
                                    
                                    
                                    /* Handle the encryption of the password before being sent to the server*/
                                    String SHAHash1 = encrypt.computeSHAHash(password);
                                    String SHAHash2 = encrypt.computeSHAHash(SHAHash1);
                                    String base64encoded = encrypt.encodeBase64(SHAHash2).toString();
                                    String finalPassword = encrypt.computeMD5Hash(base64encoded.toString());
                                    /// End encryption
                                    
                                    url = url+"/login";
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("email",email));
                                    params.add(new BasicNameValuePair("password",finalPassword));
                                    params.add(new BasicNameValuePair("from", "ugl-android"));
                                    
							        
									new JSON_Parser(mAct,params,context,"POST").execute(url);
                                                                      
                                    
                                 }
                            });
                            Dialog dialog1 = builder1.create();
                            dialog1.show();
                        }
                    }
                });
        Dialog dialog=builder.create();
        return dialog;
    }
    
    
    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
    	mConnectionResult.getErrorCode();
      if (mConnectionResult.hasResolution()) {
        try {
          mIntentInProgress = true;
          mConnectionResult.startResolutionForResult(mAct,RC_SIGN_IN);
        } catch (SendIntentException e) {
          // The intent was canceled before it was sent.  Return to the default
          // state and attempt to connect to get an updated ConnectionResult.
          mIntentInProgress = false;
          mGoogleApiClient.connect();
        }
      }
    }
    
    
         
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		  if (!mIntentInProgress) {
		    // Store the ConnectionResult so that we can use it later when the user clicks
		    // 'sign-in'.
		    mConnectionResult = result;

		    if (mSignInClicked) {
		      // The user has already clicked 'sign-in' so we attempt to resolve all
		      // errors until the user is signed in, or they cancel.
		      resolveSignInError();
		    }
		  }
	}
		  
	@Override
	public void onConnected(Bundle connectionHint) {
		mSignInClicked = false;
		Toast.makeText(mAct, "User is connected!", Toast.LENGTH_SHORT).show();
		
	}
	@Override
	public void onDisconnected() {
		
		
	}
	@Override
	public void onConnectionSuspended(int cause) {
		
		
	}
    
}
