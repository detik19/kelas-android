package net.ruangtedy.android.latih.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import net.ruangtedy.android.latih.json.model.Twitter;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.BaseAdapter;

public class JSONDemoActivity extends ListActivity {
	List<Twitter> mlist=new ArrayList<Twitter>();

	//private String murl="http://search.twitter.com/search.json?q=";
	private String muser="@detik19";
	private String mresponse;
	//private CustomAdapter adapter;
	private BaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		new GetTwitterClient().execute(muser);
	    adapter =new CustomAdapter(this, mlist);
	      // ListView lv= (ListView) findViewById(R.id.listView1);
		//   adapter 
	    setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jsondemo, menu);
		return true;
	}
	public String getResponseFromURL(String url) throws ClientProtocolException, IOException{
		String response=null;
		HttpClient httpClient=new DefaultHttpClient();
		
		HttpGet httpGet=new HttpGet(url);
		
		ResponseHandler<String> responseHandler= new BasicResponseHandler();
		
		response=httpClient.execute(httpGet,responseHandler);
		
		return response;
		
		
	}
	 /**
	    * Class extends ASynTask untuk respon URL
	    * @author Student 5
	    *
	    */
	    class GetTwitterClient extends AsyncTask<String, Void, String>{

		


			@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result==null){
				adapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}

			@Override
			protected String doInBackground(String... params) {
				
					try {
						mresponse=  getResponseFromURL("http://search.twitter.com/search.json?q="+params[0]);

						JSONObject json = new JSONObject(mresponse);
						JSONArray jArray=json.getJSONArray("results");
						for (int i=0;i<jArray.length();i++){
							json=jArray.getJSONObject(i);
							Twitter atwitter=new Twitter();
							atwitter.setFrom_user_name(json.getString("from_user_name"));
							atwitter.setText(json.getString("text"));
							atwitter.setProfile_image_url(json.getString("profile_image_url"));
							mlist.add(atwitter);
						}
						

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				
				
				return null;
			}
	    	
	    }

}
