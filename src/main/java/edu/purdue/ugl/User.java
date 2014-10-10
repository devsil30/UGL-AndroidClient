package edu.purdue.ugl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;

public class User extends Application {
	/* This is the user information*/
	static int id;
	
	static String email;
	
	static String nickname;
	
	static String firstName;
	
	static String lastName;
	
	static String avatarURL;
	
	static String phoneNumber;
	
	static String description;
	
	static boolean[] preferences = new boolean[3];
	
	static boolean isSaved = false;
	/*-----------------------------------*/
	/* This is the group information */
	static int groupId;
	static String alias;
	static String groupDescription;
	static int status;
	static int groupCreatorId;
	static int groupCount;
	static int num_of_users;
	static String groupAvatar;
	static List<Integer> groupUsers = new ArrayList<Integer>();
	static List<String> groupMemberNames = new ArrayList<String>();
	
	static List<Groups> groupsList = new ArrayList<Groups>();
	/*----------------*/
	
	
	static class Groups{
		int groupId;
		String alias;
		String groupDescription;
		int status;
		int groupCreatorId;
		int groupCount;
		int num_of_users;
		String groupAvatar;
		List<Integer> groupUsers = new ArrayList<Integer>();
		List<String> groupMemberNames = new ArrayList<String>();
	}
	
	public User(){
		
	}
	
	public static void parseUserData(JSONObject obj){
		JSONObject jData;
		try {
			jData = obj.getJSONObject("data");
			id = jData.getInt("id");
			email = jData.getString("email");
			nickname = jData.getString("nickname");
			firstName = jData.getString("first_name");
			lastName = jData.getString("last_name");
			phoneNumber = jData.getString("phone");
			description = jData.getString("description");
			avatarURL = jData.getString("avatar_url");
			JSONObject pref = jData.getJSONObject("_preferences");
			preferences[0] = pref.getBoolean("autoAcceptInvitation");
			preferences[1] = pref.getBoolean("showMyProfile");
			preferences[2] = pref.getBoolean("showMyPublicGroups");
			isSaved = true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void parseNewGroupData(JSONObject obj){
		JSONObject jData;
		JSONObject jGroup;
		JSONObject jUsers;
		JSONArray jArray;
		try{
			jData = obj.getJSONObject("data");
			jGroup = jData.getJSONObject("group_data");
			groupId = jGroup.getInt("id");
			status = jGroup.getInt("status");
			alias = jGroup.getString("alias");
			groupDescription = jGroup.getString("description");
			groupCreatorId = jGroup.getInt("creator_user_id");
			num_of_users = jGroup.getInt("num_of_users");
			jUsers = jGroup.getJSONObject("users");
			jArray = jUsers.getJSONArray("admin");
			
			for(int i =0; i < jArray.length(); i++){
				try{
					
					JSONObject oneObject = jArray.getJSONObject(i);
					groupUsers.add(oneObject.getInt("admin"));
				}
				catch(JSONException e){
					e.printStackTrace();
				}
			}
			
		} catch(JSONException e){
			e.printStackTrace();
		}
		groupCount=+1;
		addGroupsList();
	}
	
	public static void parseListGroupData(JSONObject obj){
		JSONObject jData;
		try {
			jData = obj.getJSONObject("data");
			groupCount = jData.getInt("count");
			JSONArray jArray = jData.getJSONArray("groups");
			for(int i= 0; i < jArray.length();i++){
				try{
					JSONObject oneObject = jArray.getJSONObject(i);
					
					groupId = oneObject.getInt("id");
					status = oneObject.getInt("status");
					groupDescription = oneObject.getString("description");
					alias = oneObject.getString("alias");
					groupAvatar = oneObject.getString("avatar_url");
					num_of_users = oneObject.getInt("num_of_users");
					addGroupsList();
				}
				catch (JSONException e){
					e.printStackTrace();
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void addGroupsList(){
		Groups group = new Groups();
		
		group.groupId = groupId;
		group.groupAvatar= groupAvatar;
		group.alias = alias;
		group.num_of_users = num_of_users;
		group.groupDescription = groupDescription;
		group.status = status;
		group.groupUsers = groupUsers;
		group.groupMemberNames = groupMemberNames;
		
		groupsList.add(group);
		return;
		
	}
	

}
