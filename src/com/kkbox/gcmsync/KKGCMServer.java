package com.kkbox.gcmsync;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class KKGCMServer {

	private final static String TAG = KKGCMServer.class.getSimpleName(); 
	private final static String SERVER_HOST = "http://kkgcmserver.appspot.com";    
	
	protected static interface KKConnectListener {
		void onResponse(JSONObject res);
	}

	protected static void connect(final String params, final KKConnectListener listener){
		new AsyncTask<Void, Void, HttpResponse>(){
			@Override
			protected HttpResponse doInBackground(Void... arg0) {
				final String url = SERVER_HOST + "/kkgcmserver?" + params;
				Log.d(this.getClass().getName(), url);
				final DefaultHttpClient httpclient = new DefaultHttpClient();
				final HttpGet httpget = new HttpGet(url);
				try {
					return httpclient.execute(httpget);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			protected void onPostExecute(HttpResponse res){
				if(listener != null){
					try {
						final JSONObject resJson = new JSONObject(EntityUtils.toString(res.getEntity()));
						Log.e(TAG, "resJson="+resJson);
						listener.onResponse(resJson);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.execute();
	}
	

}
