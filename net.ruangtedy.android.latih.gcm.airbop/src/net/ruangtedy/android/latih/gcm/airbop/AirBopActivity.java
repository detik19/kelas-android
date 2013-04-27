package net.ruangtedy.android.latih.gcm.airbop;
import static net.ruangtedy.android.latih.gcm.airbop.CommonUtilities.AIRBOP_APP_KEY;
import static net.ruangtedy.android.latih.gcm.airbop.CommonUtilities.AIRBOP_APP_SECRET;
import static net.ruangtedy.android.latih.gcm.airbop.CommonUtilities.GOOGLE_PROJECT_NUMBER;
import static net.ruangtedy.android.latih.gcm.airbop.CommonUtilities.SERVER_URL;
import static net.ruangtedy.android.latih.gcm.airbop.CommonUtilities.USE_SERVICE;
import static net.ruangtedy.android.latih.gcm.airbop.CommonUtilities.displayMessage;

import com.google.android.gcm.GCMRegistrar;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;

public class AirBopActivity extends Activity implements AirBopRegisterTask.RegTaskCompleteListener, LocationListener  {

	public static final long AIRBOP_DEFAULT_ON_SERVER_LIFESPAN_MS=1000L*3600L*24L*21L;
	private AirBopRegisterTask mRegisterTask=null;
	private AsyncTask<Void, Void, Void> mUnRegisterTask=null;
	protected AirBopServerUtilities mServerData=null;
	private AirBopRegisterReceiver mRegisterReceiver=null;
	protected boolean mServiceRunning=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(AIRBOP_APP_KEY, "APP_KEY");
		checkNotNull(AIRBOP_APP_SECRET, "APP_SECRET");
		
		//Make sure the device has the proper dependencies
		GCMRegistrar.checkDevice(this);
		

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		
		//Ser the AirBop registration lifespan to be 21 days. This means that after 14 days
        //this client will attempt to reregister with AirBop. 
        //Make sure that this value is positive, otherwise you will run into issues
		GCMRegistrar.setRegisterOnServerLifespan(this, AIRBOP_DEFAULT_ON_SERVER_LIFESPAN_MS);
		
		mServerData=AirBopServerUtilities.fillDefaults("");
		mServerData.mLabel="AirBop Sample";
		
	}

	protected void register(final boolean withLocation){
		if(withLocation){
			
			Location location=CommonUtilities.getLastLocation(this);
			if(location==null){
    			// We didn't get the location
				displayMessage(this, getString(R.string.getting_location));
				
				// We have to query the Location Manager for the location
    			// and get the result in onLocationChanged
				CommonUtilities.getCurrentLocation(this, this);
			
			}
			else{
				//save the data to the prefs
				mServerData.mLocation=location;
				mServerData.saveCurrentDataToPrefs(getApplicationContext());
				
				//register
				internalRegister();
			}
		}
		else{
			//Remove any old locatiom ndata
			AirBopServerUtilities.clearLocationPrefs(getApplicationContext());
			//Save the label if it's there
			mServerData.saveCurrentDataToPrefs(getApplicationContext());
			//register
			internalRegister();
		}
	}
	
	public void internalRegister(){
		final Context appContext=getApplicationContext();
		final String regId=GCMRegistrar.getRegistrationId(appContext);
		
		if(regId.equals("")){
			// We don't have a REG ID from GCM so let's ask for one. 
        	// The response from the GCM server will trigger: 
        	// GCMIntentService.onRegistered() where we will then register with
        	// AiRBop's servers.
			displayMessage(appContext, getString(R.string.gcm_register_attempt));
			GCMRegistrar.register(appContext, GOOGLE_PROJECT_NUMBER);
			
		}
		else{
			// We have a regID from the GCM, now check to see if
        	// we have successfully registered with AirBop's servers:
			
			if(GCMRegistrar.isRegisteredOnServer(appContext)){
				
				// This device is already registered with GCM and with the AirBop
            	// servers, nothing to do here.
				displayMessage(appContext, getString(R.string.already_registered));
				
				return;
				
			}
			else if(USE_SERVICE){
            	//Register with a service
				internalRegisterService(regId);
			}
			else if(mRegisterTask==null){
				//Regoister with
				internalRegisterAsyncTask(regId);

			}
			else{
				displayMessage(appContext, getString(R.string.reg_thread_running));
			}
			
		}
		
	}
	
	private void internalRegisterService(final String regId){
		if(mServiceRunning==false){
			// We have previously gotten the regID from the GCM server, but
        	// when we tried to register with AirBop in the
        	// GCMIntentService.onRegistered() callback it failed, so let's try again
        	
            // This will be done outside of the GUI thread.
			if(mRegisterReceiver==null){
				//register receiver
				registerAirBopRegisterReceiver();
			}
			
			Intent intent=new Intent(this, AirbopIntentService.class);
			intent.putExtra(AirbopIntentService.BUNDLE_REG_ID, regId);
			intent.putExtra(AirbopIntentService.BUNDLE_REGISTRATION_TASK, true);
			
			mServiceRunning=true;
			
			//Start Service
			startService(intent);
			
		}
		else{
			displayMessage(this, getString(R.string.reg_thread_running));
		}
	}

	private void internalRegisterAsyncTask(final String regId){
		if(mRegisterTask==null){
			 
        	// We have previously gotten the regID from the GCM server, but
        	// when we tried to register with AirBop in the
        	// GCMIntentService.onRegistered() callback it failed, so let's try again
        	
            // This will be done outside of the GUI thread.
        	
            // It's also necessary to cancel the thread onDestroy(),
            // hence the use of AsyncTask instead of a raw thread. 
			mServerData.mRegId=regId;
			mRegisterTask=new AirBopRegisterTask(this, getApplicationContext(),regId, mServerData);
			mRegisterTask.execute(null, null, null);
			
		}
		else{
			displayMessage(this, getString(R.string.reg_thread_running));
		}
	}
	public void unRegister(){
		if(USE_SERVICE){
			//use the service
			internalUnRegisterService();
		}
		else{
			//use ASYNC TASK
			internalUnregisterAsynckTask();
		}

	}
	
	private void internalUnRegisterService(){
		if(mServiceRunning==false){
			final String regId=GCMRegistrar.getRegistrationId(this);
			// Only bother if we actually have a regID from GCM, otherwise
	    	// there is nothing to unregister
			if(!regId.equals("")){
				if(mRegisterReceiver==null){
					//Register receiver
					registerAirBopRegisterReceiver();
				}
				
				Intent intent =new Intent(this, AirbopIntentService.class);
				intent.putExtra(AirbopIntentService.BUNDLE_REG_ID, regId);
				intent.putExtra(AirbopIntentService.BUNDLE_REGISTRATION_TASK, false);
				
				mServiceRunning=true;
				
				//Start Service
				startService(intent);
				
			}
		}
		else{
			displayMessage(this, getString(R.string.unreg_thread_running));
		}
	}
	
	private void internalUnregisterAsynckTask(){
		// Try to unregister, but not in the UI thread.
        // It's also necessary to cancel the thread onDestroy(),
        // hence the use of AsyncTask instead of a raw thread.
		final Context appContext=getApplicationContext();
		
		//use the ASYNC TASK\
		if(mUnRegisterTask==null){
			final String regId=GCMRegistrar.getRegistrationId(this);
			// Only bother if we actually have a regID from GCM, otherwise
	    	// there is nothing to unregister
			if(!regId.equals("")){
				
				mUnRegisterTask=new AsyncTask<Void, Void, Void>(){

					@Override
					protected Void doInBackground(Void... params) {
						
						boolean unregistered=AirBopServerUtilities.unregister(appContext, regId);
		                // If this worked unregister from the GCM servers

						if(unregistered){
							GCMRegistrar.unregister(appContext);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mUnRegisterTask=null;
					}
					
				};
				
				mUnRegisterTask.execute(null, null, null);
			}
			else{
				displayMessage(appContext, getString(R.string.unreg_thread_running));
			}
		}
	}
	
	
	@Override
	protected void onDestroy() {
		if(mRegisterTask!=null){
			mRegisterTask.cancel(true);
			
		}
		if(mUnRegisterTask!=null){
			mUnRegisterTask.cancel(true);
			
		}
		unregisterAirBopRegisterReceiver();
		unregisterFromLocationManager();
        GCMRegistrar.onDestroy(getApplicationContext());
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// Unregister from location manager
		unregisterFromLocationManager();
		displayMessage(this, getString(R.string.got_current_location,location.getLatitude(), location.getLongitude()));
		
		// Set the location and save the data so that the intent services can read
		// it
		mServerData.mLocation = location;
		mServerData.saveCurrentDataToPrefs(getApplicationContext());
		//register
		internalRegister();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskComplete() {

		mRegisterTask=null;
	}
	
	
	private void unregisterFromLocationManager(){
		LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(locationManager!=null){
			locationManager.removeUpdates(this);
			
		}
	}
	
	private void checkNotNull(Object reference, String name){
		if(reference==null){
			throw new NullPointerException(getString(R.string.error_config, name));
		}
	}
	
	public void registerAirBopRegisterReceiver(){
		
		if(mRegisterReceiver==null){
			//Receiver
			IntentFilter filter=new IntentFilter(AirbopIntentService.ACTION_REGISTRATION_PROCESS);
			
			filter.addCategory(Intent.CATEGORY_DEFAULT);
			mRegisterReceiver=new AirBopRegisterReceiver();
			registerReceiver(mRegisterReceiver, filter);
		}
	}
	
	protected void unregisterAirBopRegisterReceiver(){
		if(mRegisterReceiver!=null){
			unregisterReceiver(mRegisterReceiver);
			mRegisterReceiver=null;
		}
	}
	public class AirBopRegisterReceiver extends BroadcastReceiver{

		
		@Override
		public void onReceive(Context context, Intent intent) {
			mServiceRunning=false;
			boolean registration_task=intent.getBooleanExtra(AirbopIntentService.BUNDLE_REGISTRATION_TASK, true);
			if(registration_task){
				displayMessage(context, getString(R.string.airbop_service_registration_complete));
				
			}
			else{
				displayMessage(context, getString(R.string.airbop_service_unregistration_complete));
				
			}
			

		}
		
	}

}
