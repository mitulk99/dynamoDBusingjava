package com.maa.v2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
 public class Coords {
	//Map<String,String> ll;
	public String Lat(String pincode)
	{
		//Double lat=0.0;
		String lat="";
		try {
			
			String url = "https://nominatim.openstreetmap.org/search?q=";
		     URL obj = new URL(url+pincode+"&format=json");
		     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		     // optional default is GET
		     
//		     con.setRequestMethod("GET");
		     //add request header
		    // con.setRequestProperty("User-Agent", "Mozilla/5.0");
		     int responseCode = con.getResponseCode();
		   //  System.out.println("\nSending 'GET' request to URL : " + url+ pincode+ "&format=json");
		   //  System.out.println("Response Code : " + responseCode);
		     BufferedReader in = new BufferedReader(
		             new InputStreamReader(con.getInputStream()));
		     String inputLine;
		     StringBuffer response = new StringBuffer();
		     while ((inputLine = in.readLine()) != null) {
		     	response.append(inputLine);
		     }
		     in.close();
		     //print in String
		   //  System.out.println(response.toString());
		     
		     JSONArray myresponse= new JSONArray(response.toString());
		     JSONObject object = myresponse.getJSONObject(0);
//		     ll.put("Lat",object.getString("lat"));
//		     ll.put("Long",object.getString("lon"));
		   //  lat=Double.valueOf(object.getString("lat"));
		     lat=object.getString("lat");
		    // System.out.println(lat);
		   //  System.out.println("Lat : " +Double.valueOf(object.getString("lat")));
		  //  System.out.println("Long : " +Double.valueOf(object.getString("lon")));
		    // JSONArray array = new JSONArray(myresponse.getJSONArray("0"));
		    // System.out.println(object);
		   //  System.out.println("-- "+myresponse.length());
		   
			}
		     catch (Exception e) {
				System.out.println(e);
				}
		finally
		{
			return lat;
		}
	}
	public String Long(String pincode)
	{
		//Double lon=0.0;
		String lon="";
		try {
			
			String url = "https://nominatim.openstreetmap.org/search?q=";
		     URL obj = new URL(url+pincode+"&format=json");
		     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		     // optional default is GET
		     
//		     con.setRequestMethod("GET");
		     //add request header
		    // con.setRequestProperty("User-Agent", "Mozilla/5.0");
		     int responseCode = con.getResponseCode();
		    // System.out.println("\nSending 'GET' request to URL : " + url+ pincode+ "&format=json");
		    // System.out.println("Response Code : " + responseCode);
		     BufferedReader in = new BufferedReader(
		             new InputStreamReader(con.getInputStream()));
		     String inputLine;
		     StringBuffer response = new StringBuffer();
		     while ((inputLine = in.readLine()) != null) {
		     	response.append(inputLine);
		     }
		     in.close();
		     //print in String
		   //  System.out.println(response.toString());
		     
		     JSONArray myresponse= new JSONArray(response.toString());
		     JSONObject object = myresponse.getJSONObject(0);
//		     ll.put("Lat",object.getString("lat"));
//		     ll.put("Long",object.getString("lon"));
		  //   lon=Double.valueOf(object.getString("lon"));
		     lon=object.getString("lon");
		    // System.out.println(lat);
		   //  System.out.println("Lat : " +Double.valueOf(object.getString("lat")));
		  //  System.out.println("Long : " +Double.valueOf(object.getString("lon")));
		    // JSONArray array = new JSONArray(myresponse.getJSONArray("0"));
		    // System.out.println(object);
		   //  System.out.println("-- "+myresponse.length());
		   
			}
		     catch (Exception e) {
				System.out.println(e);
				}
		finally
		{
			return lon;
		}
	}

}
