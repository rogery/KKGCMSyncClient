package com.kkbox.gcmsync;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

public class GCMIntentService extends GCMBaseIntentService {
	
	private final static String TAG = GCMIntentService.class.getSimpleName();
	private final static String SENDER_ID = "885901097683";
	
	private static KKGCMListener listener;
	
	protected static interface KKGCMListener {
		void onUpdate();
	}
	
	protected static void registerGCM(Context context) {
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		
		final String regId = GCMRegistrar.getRegistrationId(context);
		if (regId.equals("")) {
			GCMRegistrar.register(context, SENDER_ID);
		} else {
			KKDataServer.connect("act=register&regId="+regId, null);
			Log.i(TAG, "Already registered: " + regId);
		}
	}

	protected static void registerGCMListener(KKGCMListener gcmListener){
		listener = gcmListener;
	}
	
	public GCMIntentService() {
        super(SENDER_ID);
	}
	
	@Override
	  protected String[] getSenderIds(Context context) {
	     return new String[]{ SENDER_ID };
	  }
	
	@Override
	protected void onError(Context context, String arg1) {
		Log.e(TAG, "onError: " + arg1);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "onMessage: " + intent);
		
		final String message = intent.getExtras().getString("message");
		if("sync".equals(message)){
			listener.onUpdate();
		}
	}

	@Override
	protected void onRegistered(Context ctx, String regId) {
		Log.i(TAG, "onRegistered: " + regId);
		KKDataServer.connect("act=register&regId="+regId, null);
	}
	
	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.i(TAG, "onUnregistered: " + arg1);
	}

}
