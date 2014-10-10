package edu.purdue.ugl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class registerDialog extends DialogFragment {
    Activity mAct;
    
    EditText nameText;
    EditText userNameText;
    EditText passwordText;
    EditText passwordText2;
    
    private EncryptHelper encrypt;
    
    String url = "http://ugl.sige.us/api/register";
    
    //JSON_Parser jsonp = new JSON_Parser("http://ugl.sige.us/api/register");
    @Override
    public void onAttach(Activity act){
        super.onAttach(act);
        mAct=act;
        
        encrypt = new EncryptHelper();
        
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.registerw)
                .setItems(R.array.account_types, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if(which==0){
                            Toast.makeText(getActivity(),"Google+",Toast.LENGTH_SHORT).show();
                        }else if(which==1){
                            Toast.makeText(getActivity(),"todo",Toast.LENGTH_SHORT).show();
                        }else if(which==2){
                            dialog.dismiss();
                            AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater=getActivity().getLayoutInflater();
                            View view = inflater.inflate(R.layout.register_dialog,null);
                            nameText = (EditText) view.findViewById(R.id.name);
                            userNameText = (EditText) view.findViewById(R.id.email);
                            passwordText = (EditText) view.findViewById(R.id.password);
                            passwordText2 = (EditText) view.findViewById(R.id.reenterpassword);
                            builder1.setView(view);
                            builder1.setTitle("Register with Email Address");
                            builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    dialog1.dismiss();
                                }
                            });
                            builder1.setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                	Editable fullname = nameText.getText();
                                	Editable username = userNameText.getText();
                                	Editable password = passwordText.getText();
                                	Editable password2 = passwordText2.getText();
                                	String name = fullname.toString();
                                	String email = username.toString();
                                	String pass = password.toString();
                                	String pass2 = password2.toString();
                                	
                                	Context context = mAct.getApplicationContext();
                                	
                                	String firstName = "";
                                	String lastName = "";
                                	// split name box and set to first and last name
                                	if(name.contains(" ")){
                                		String[] name_array = name.split(" ");
                                		firstName = name_array[0];
                                		lastName = name_array[1];
                                	}
                                	else{
                                		firstName = name;
                                	}
                                	
                                	// Encrypt password 
                                	String sha11 = encrypt.computeSHAHash(pass);
                                	String sha12 = encrypt.computeSHAHash(pass2);
                                	String sha21 = encrypt.computeSHAHash(sha11);
                                	String sha22 = encrypt.computeSHAHash(sha12);
                                	String base641 = encrypt.encodeBase64(sha21);
                                	String base642 = encrypt.encodeBase64(sha22);
                                	
                                	// FInal password
                                	String finalPassword = encrypt.computeMD5Hash(base641);
                                	String finalPassword2 = encrypt.computeMD5Hash(base642);
                                	// End encrypt                   	
                                	
                                	
                                	if(pass.equals(pass2) && pass.length()>6){
                                		List<NameValuePair> params = new ArrayList<NameValuePair>();
                                		params.add(new BasicNameValuePair("from","ugl_android"));
                                		params.add(new BasicNameValuePair("first_name",firstName));
                                		params.add(new BasicNameValuePair("last_name",lastName));
                                		params.add(new BasicNameValuePair("email",email));
                                		params.add(new BasicNameValuePair("password",finalPassword));
                                		params.add(new BasicNameValuePair("confirm_pass",finalPassword2));
                                		params.add(new BasicNameValuePair("agree", "true"));
                                		
                                		new JSON_Parser(mAct,params,context,"POST").execute(url);
                                		
                                        Toast.makeText(mAct,"Registration complete. Please check your email.",Toast.LENGTH_LONG).show();
                                        dialog1.dismiss();
                                	}
                                	else{
                                		Toast.makeText(mAct,"Passwords do not match or is not long enough", Toast.LENGTH_SHORT).show();
                                		passwordText.setBackgroundColor(Color.RED);
                                		passwordText2.setBackgroundColor(Color.RED);
                                	}
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
}
