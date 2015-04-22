package com.jcoolstory.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.jcoolstory.crackbinidemo.GameResult;


public class WebUtil {
	public final static String REQUEST_VIEW = "http://crackbinius.appspot.com/crackbinius/bini_score";
	public final static String REQUEST_UPLOAD = "http://crackbinius.appspot.com/crackbinius/db_score";
	public String RequestScore(String urlstr) {
		StringBuilder html = new StringBuilder();
		try {
			
			
			 HttpClient client = new DefaultHttpClient();  
		        ClientConnectionManager ccm= client.getConnectionManager();
		        ccm.closeIdleConnections(5000, TimeUnit.MILLISECONDS);
		        HttpPost post = new HttpPost(REQUEST_VIEW);
		        

		        
//		        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
//		        post.setEntity(ent);
		        
		        HttpResponse responsePOST = client.execute(post);
		        
		        HttpEntity resEntity = responsePOST.getEntity();
		        if (resEntity != null)
		        {    
		        	html.append(EntityUtils.toString(resEntity, "UTF-8"));
//		        	Log.i("RESPONSE", EntityUtils.toString(resEntity));
		        }
		        
			
//			URL url = new URL(REQUEST_VIEW);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			if (conn != null) {
//				conn.setConnectTimeout(10000);
//				conn.setUseCaches(false);
//				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//			
////					if (check == true
////							&& conn.getContentType().contains("text/xml")) {
//						BufferedReader br = new BufferedReader(
//								new InputStreamReader(conn.getInputStream(),
//										"EUC-KR"));
//						for (;;) {
//							String line = br.readLine();
//
//							if (line == null)
//								break;
//							html.append(line + '\n');
//						}
//						Log.d("TAG",html.toString());
//						br.close();
//				} else {
//
//				}
//				conn.disconnect();
//			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return html.toString();
    }
	public ArrayList<Map<String,String>> parserScore(String str)
	{
		try
		{
			String lines[] = str.split("\n");
			StringBuilder sb = new StringBuilder();
			for (String strbuffer : lines)
			{
				sb.append(strbuffer);
			}
			Log.d("TAG", sb.toString());
			ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
			int size = lines.length;
			for (int i = 0 ; i < size ; i++)
			{
				lines[i] = lines[i].replace("{", "");
				lines[i] = lines[i].replace("}","");
				String fields[] = lines[i].split(",");
				Map<String,String> map = new HashMap<String, String>();
				map.put("rank",fields[0]);
				map.put("user_name",fields[1]);
				map.put("score", fields[2]);
				map.put("date", fields[3]);
				list.add(map);
			}
			
			return list;
		}
		catch (Exception e)
		{
			
		}
		return null;
	}
	public int upload(GameResult gameResult)
	{
		URL url;
		try {
			url = new URL(REQUEST_UPLOAD);

	        HttpClient client = new DefaultHttpClient();  
	        ClientConnectionManager ccm= client.getConnectionManager();
	        ccm.closeIdleConnections(5000, TimeUnit.MILLISECONDS);
	        HttpPost post = new HttpPost(REQUEST_UPLOAD);
	        
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("user_name", gameResult.User_name));
	        params.add(new BasicNameValuePair("score", String.valueOf(gameResult.final_score)));
	        
	        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
	        post.setEntity(ent);
	        
	        HttpResponse responsePOST = client.execute(post);  
	        HttpEntity resEntity = responsePOST.getEntity();
	        
	        if (resEntity != null)
	        {    
	        	Log.i("RESPONSE", EntityUtils.toString(resEntity));
	        }
	        return 1;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
