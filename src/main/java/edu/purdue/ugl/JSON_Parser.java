package edu.purdue.ugl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class JSON_Parser extends AsyncTask<String,Void,JSONObject>{
	
	static InputStream inputStream;
	static JSONObject jObj = null;
	JSONObject jObject;
	static String json = "";
	
	String method;
	
	private List<NameValuePair> list;
	private Context mContext;
	
	private Activity activity;
	
	private ProgressDialog dialog;
	
	User userInfo;
	
	// constructor for logging in and registering users
	public JSON_Parser(Activity act, List<NameValuePair> list, Context context,String method) {
		this.method = method;
		this.list = list;
		this.mContext = context;
		this.activity = act;
	}
	
	/*
	@Override
	protected void onPreExecute(){
		dialog = ProgressDialog.show(mContext, "Loading", "Loading User Data");
	}
*/


	@Override
	protected JSONObject doInBackground(String... params) {
		//HttpResponse httpResponse = null;
		try{
			if(method == "POST"){
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);
				httpPost.setEntity(new UrlEncodedFormEntity(list));
				
				HttpResponse httpResponse = httpClient.execute(httpPost);
				StatusLine statusLine = httpResponse.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if(statusCode == 200){
					HttpEntity httpEntity = httpResponse.getEntity();
					inputStream = httpEntity.getContent();
				}
				else{
					Log.e("LOG", "Failed to download result..");
				}
				
				Header contentencoding = httpResponse.getFirstHeader("Content-Encoding");
				if(contentencoding != null && contentencoding.getValue().equalsIgnoreCase("gzip")){
					inputStream = new GZIPInputStream(inputStream);
				}
				
				BufferedReader mBufferedReader = new BufferedReader( new InputStreamReader(inputStream, "iso-8859-1"),8);
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				while((line = mBufferedReader.readLine()) != null ){
					stringBuilder.append(line +"\n");
				}
				inputStream.close();
				json = stringBuilder.toString();
					
				jObj = new JSONObject(json);
				
			}
			/*
			else if(method == "GET"){
				
				HttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(list, "utf-8");
				Dataurl+= "?" +paramString;
				HttpGet httpGet = new HttpGet(Dataurl);
				
				httpResponse = httpClient.execute(httpGet);
				StatusLine statusLine = httpResponse.getStatusLine();
				if()
				
				HttpEntity httpEntity = httpResponse.getEntity();
				inputStream = httpEntity.getContent();
			}*/
			else{
				Log.e("Request Alert","Wrong Method Name");
			}
		}
		catch(UnsupportedEncodingException e){
			Log.e("UnsupportEncodingException", e.getMessage().toString());
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocalException", e.getMessage().toString());
		} catch (IOException e) {
			Log.e("IOException",e.getMessage().toString());
		} catch (JSONException e) {
			Log.e("JSONException", e.getMessage().toString());
		}
		
		return jObj;
		
	}

	@Override
	protected void onPostExecute(JSONObject obj){
		 try {
			String status = obj.getString("status");
			if(status.equals("error")){
				int errCode = obj.getInt("error");
				String mess = obj.getString("message");
				Toast.makeText(mContext, mess + "Error Code: " + errCode, Toast.LENGTH_SHORT).show();
				//Intent intent = new Intent(mContext, MainActivity.class);
				//mContext.startActivity(intent);
			}
			else{
				if((activity  instanceof MainActivity)){
					Intent intent = new Intent(mContext,MainPage.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("JSONObject", obj.toString());
					mContext.startActivity(intent);
				}
				else if(activity instanceof MainPage && User.isSaved == false){
					User.parseUserData(obj);
				}
				else if(activity instanceof MainPage && User.isSaved == true){
					User.parseListGroupData(obj);
				}
				else if(activity instanceof EditProfile){
					User.parseUserData(obj);
				}
				else if(activity instanceof newGroup){
					User.parseNewGroupData(obj);
				}
				else if (activity instanceof FragmentActivity){
					// do nothing
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
