package net.ruangtedy.android.latih.gcm.airbop;

import com.google.android.gcm.GCMRegistrar;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AirbopIntentService extends IntentService {

	public static final String ACTION_REGISTRATION_PROCESS="com.airbop.client.intent.action.REGISTRATION_PROCESSED";
	public static final String BUNDLE_REG_ID="REG_ID";
	public static final String BUNDLE_AIRBOP_SUCCESS="AIRBOP_SUCCESS";
	public static final String BUNDLE_REGISTRATION_TASK="REG_TASK";
	
	public AirbopIntentService() {
		super("AirBopIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Context appContext=getApplicationContext();
		boolean registered=false;
		boolean register_task=true;
		
		if(appContext!=null){
			registered=GCMRegistrar.isRegisteredOnServer(appContext);
			
			Bundle extras=intent.getExtras();
			String regID=null;
			
			if(extras!=null){
				regID=extras.getString(BUNDLE_REG_ID);
				register_task=extras.getBoolean(BUNDLE_REGISTRATION_TASK, true);
				
			}
			
			if(regID!=null){
				if(register_task){
					if(!registered){
						AirBopServerUtilities server_data=AirBopServerUtilities.fillDefaults(regID);
						server_data.loadDataFromPrefs(appContext);
						
						// Get rid of the location from the prefs so we requery next time

						AirBopServerUtilities.clearLocationPrefs(appContext);
						
						registered=AirBopServerUtilities.register(appContext, server_data);
						// At this point all attempts to register with the AirBop
					    // server failed, so we need to unregister the device
					    // from GCM - the app will try to register again when
					    // it is restarted. Note that GCM will send an
					    // unregistered callback upon completion, but
					    // GCMIntentService.onUnregistered() will ignore it.
						if(!registered){
							GCMRegistrar.unregister(appContext);
						}
					}
				}
				else {
					
					if(registered){
						registered=AirBopServerUtilities.unregister(appContext, regID);
		                // If this worked unregister from the GCM servers
						if(registered){
							GCMRegistrar.unregister(appContext);
						}

					}
					
				}
					
			}
		}
		
		Intent broadcastIntent=new Intent();
		broadcastIntent.setAction(ACTION_REGISTRATION_PROCESS);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(BUNDLE_AIRBOP_SUCCESS, registered);
		broadcastIntent.putExtra(BUNDLE_REGISTRATION_TASK, register_task);
		
		sendBroadcast(broadcastIntent);
	}

}
