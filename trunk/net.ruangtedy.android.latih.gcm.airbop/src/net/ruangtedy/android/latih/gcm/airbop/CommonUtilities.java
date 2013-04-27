package net.ruangtedy.android.latih.gcm.airbop;

import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class CommonUtilities {
	/**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */ 
    static final String SERVER_URL = "http://www.airbop.com/api/v1/";
    /**
     * Google Project Number registered to use GCM (from your Google API Console).
     */
    static final String GOOGLE_PROJECT_NUMBER = "819177838989";
	/**
     * AirBop App key to identify this app
     */
    static final String AIRBOP_APP_KEY = "c59ca557-028d-4057-8f3d-699836506b6a";
    
    /**
     * AIRBOP_APP_SECRET App key to identify this app shhhh
     */
    static final String AIRBOP_APP_SECRET = "1e85f608503e90b59136dc5e1c11114d5682078a0e81c56522a9d1bc06972292";
	
    
	/** Should we send the location to the AirBopServer
     * If you set this value to true, you also need to uncomment the following manifest permissions:
     * android.permission.ACCESS_FINE_LOCATION
     * android.permission.ACCESS_COARSE_LOCATION
     */
    static final boolean USE_LOCATION = false;
    
    /**
     * Should we use the IntentService or the AsyncTask
     */
    static final boolean USE_SERVICE = true;
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */

    /**
     * Intent's extra that contains the message to be displayed.
     */
    static final String EXTRA_MESSAGE = "message";
    /**
     * Intent used to display a message in the screen.
     */
    static final String DISPLAY_MESSAGE_ACTION =
            "com.airbop.client.app.DISPLAY_MESSAGE";
    
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
    
    static void displayMessageFromIntent(Context context, Intent intent){
    	if(intent!=null){
    		Bundle bundle=intent.getExtras();
    		if(bundle!=null){
    			Set<String> keys=bundle.keySet();
    			if(keys!=null){
    				for(String key : keys){
    					Object o = bundle.get(key);
    					if(o!=null){
    						displayMessage(context, "Key:"+key+" value"+o);
    					}
    				}
    			}
    		}
    		else{
    			displayMessage(context, "Extras are null");
    		}
    	}
    	else {
    		displayMessage(context, "Intent is null");
    	}
    }
    /************************
     * Language helpers
     */
    
    /**
     * Simple helper that gets the location criteria that we want. 
     * @return
     */
    public static Criteria getCriteria(){
    	if(USE_LOCATION){
    		Criteria criteria =new Criteria();
    		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
    		criteria.setPowerRequirement(Criteria.POWER_LOW);
    		criteria.setAltitudeRequired(false);
    		criteria.setBearingRequired(false);
    		criteria.setSpeedRequired(false);
    		criteria.setCostAllowed(true);
    		return criteria;
    	}
		return null;
    	
    }
    /**
     * Get the last location from the LocationManager, if it's available, if not
     * return null.
     * @param appContext
     * @return
     */
    public static Location getLastLocation(final Context appContext){
    	Location location=null;
    	if(USE_LOCATION){
    		Criteria criteria=getCriteria();
    		LocationManager locationManager=(LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
    		if(locationManager!=null){
    			String provider = locationManager.getBestProvider(criteria, true);
    			location=locationManager.getLastKnownLocation(provider);
    			if(location!=null){
    				displayMessage(appContext, appContext.getString(R.string.got_last_location, location.getLatitude(), location.getLongitude()));
    				
    			}
    			
    		}
    	}
		return location;
    	
    }
    
    public static boolean getCurrentLocation(LocationListener locationListener, final Context appContext){
		if(USE_LOCATION){
			Criteria criteria=getCriteria();
			LocationManager locationManager=(LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
			if(locationManager!=null){
				String provider = locationManager.getBestProvider(criteria, true);
				
				locationManager.requestLocationUpdates(provider, 200, 10, locationListener);
				return true;
				
			}
			
		}
    	
    	return false;
    	
    }
}
