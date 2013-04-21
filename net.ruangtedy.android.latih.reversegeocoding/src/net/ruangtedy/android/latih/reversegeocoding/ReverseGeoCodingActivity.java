package net.ruangtedy.android.latih.reversegeocoding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReverseGeoCodingActivity extends Activity {
	private TextView mLatlng;
	private TextView mAddress;
	private Button mFineProviderButton;
	private Button mBothProviderButton;
	private LocationManager mLocationManager;
	private Handler mHandler;
	
	private boolean mGeoCoderAvailable;
	private boolean mUseFine;
	private boolean mUseBoth;
	
	//keys for maintaining UI states after rotation
	private static final String KEY_FINE="use_fine";
	private static final String KEY_BOTH="use_both";
	
	//UI handler
	private static final int UPDATE_ADDRESS	=1;
	private static final int UPDATE_LATLNG	=2;
	
	private static final int TEN_SECONDS	=1000;
	private static final int TEN_METERS		=10;
	private static final int TWO_MINUTES	=1000*60*2;
	
	
	
	 /**
     * This sample demonstrates how to incorporate location based services in your app and
     * process location updates.  The app also shows how to convert lat/long coordinates to
     * human-readable addresses.
     */

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reverse_geo_coding);
		
		 // Restore apps state (if exists) after rotation.
        if (savedInstanceState != null) {
            mUseFine = savedInstanceState.getBoolean(KEY_FINE);
            mUseBoth = savedInstanceState.getBoolean(KEY_BOTH);
        } else {
            mUseFine = false;
            mUseBoth = false;
        }
        
		mLatlng=(TextView) findViewById(R.id.latlng);
		mAddress=(TextView) findViewById(R.id.address);
		
		mFineProviderButton=(Button) findViewById(R.id.btn_provider_fine);
		mBothProviderButton=(Button) findViewById(R.id.btn_provider_both);
		
		mGeoCoderAvailable= Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent();
		
		mHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what){
					case UPDATE_ADDRESS:
						mAddress.setText((String) msg.obj);
						break;
					case UPDATE_LATLNG:
						mLatlng.setText((String) msg.obj);
						break;
				
				}
			}
			
		};
		
		mLocationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		mFineProviderButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  mUseFine = true;
			        mUseBoth = false;
			        setup();				
			}
		});
		
		mBothProviderButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mUseFine = false;
		        mUseBoth = true;
		        setup();				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reverse_geo_coding, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_FINE, mUseFine);
		outState.putBoolean(KEY_BOTH, mUseBoth);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 setup();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Check if the GPS setting is currently enabled on the device.
        // This verification should be done during onStart() because the system calls this method
        // when the user returns to the activity, which ensures the desired location provider is
        // enabled each time the activity resumes from the stopped state.
        LocationManager locationManager =  (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
          // new EnableGpsDialogFragment().show(getSupportFragmentManager(), "enableGpsDialog");
        }
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
        mLocationManager.removeUpdates(listener);

	}
	 // Method to launch Settings
    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }
    
    // Set up fine and/or coarse location providers depending on whether the fine provider or
    // both providers button is pressed.
	private void setup(){
		Location gpsLocation=null;
		Location networkLocation=null;
		
		mLocationManager.removeUpdates(listener);
		mLatlng.setText(R.string.unknown);
		mAddress.setText(R.string.unknown);
		
		if(mUseFine){
			gpsLocation=requestUpdateFromProvider(LocationManager.GPS_PROVIDER, R.string.not_support_gps);
			if(gpsLocation!=null)
				updateUILocation(gpsLocation);
		}
		else if(mUseBoth){
			gpsLocation=requestUpdateFromProvider(LocationManager.GPS_PROVIDER, R.string.not_support_gps);
			networkLocation=requestUpdateFromProvider(LocationManager.NETWORK_PROVIDER, R.string.not_support_network);
			
			if(gpsLocation!=null && networkLocation !=null){
				updateUILocation(getBetterLocation(gpsLocation, networkLocation));
			}
			else if(gpsLocation!=null){
				updateUILocation(gpsLocation);
				
			}
			else if(networkLocation!=null){
				updateUILocation(networkLocation);
			}
		}
		
	}
	
	 /**
     * Method to register location updates with a desired location provider.  If the requested
     * provider is not available on the device, the app displays a Toast with a message referenced
     * by a resource id.
     *
     * @param provider Name of the requested provider.
     * @param errorResId Resource id for the string message to be displayed if the provider does
     *                   not exist on the device.
     * @return A previously returned {@link android.location.Location} from the requested provider,
     *         if exists.
     */
	private Location requestUpdateFromProvider(final String provider, final int errorResId ){
		Location location=null;
		if(mLocationManager.isProviderEnabled(provider)){
			mLocationManager.requestLocationUpdates(provider, TEN_SECONDS, TEN_METERS, listener );
			location=mLocationManager.getLastKnownLocation(provider);
		}
		else{
			Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show();
		}
		return location;
	}

	private void updateUILocation(Location location){
		// We're sending the update to a handler which then updates the UI with the new
        // location.
		Message.obtain(mHandler, UPDATE_LATLNG,location.getLatitude()+","+location.getLongitude()).sendToTarget();
		
		if(mGeoCoderAvailable)
			doReverseGeoCoding(location);
			
	}
	
	private void doReverseGeoCoding(Location location){
		// Since the geocoding API is synchronous and may take a while.  You don't want to lock
        // up the UI thread.  Invoking reverse geocoding in an AsyncTask.
		(new ReverseGeocodingTask(this)).execute(new Location[] {location});
	}
	
	private final LocationListener listener=new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// A new location update is received.  Do something useful with it.  Update the UI with
            // the location update.
			updateUILocation(location);
			
		}
	};
	 /** Determines whether one Location reading is better than the current Location fix.
     * Code taken from
     * http://developer.android.com/guide/topics/location/obtaining-user-location.html
     *
     * @param newLocation  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new
     *        one
     * @return The better Location object based on recency and accuracy.
     */
	protected Location getBetterLocation(Location newlocation, Location currentBestLocation){

		if(currentBestLocation==null){
            // A new location is always better than no location
			return newlocation;
		}
		
        // Check whether the new location fix is newer or older
		long timeDelta=newlocation.getTime()-currentBestLocation.getTime();
		boolean isSignificantlyNewer=timeDelta>TWO_MINUTES;
		boolean isSignificantlyOlder=timeDelta<-TWO_MINUTES;
		boolean isNewer=timeDelta>0;
		

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved.
		if(isSignificantlyNewer){
			return newlocation;
	     // If the new location is more than two minutes older, it must be worse\
		}
		else if (isSignificantlyOlder){
			return currentBestLocation;
		}
		
        // Check whether the new location fix is more or less accurate
		int accuracyDelta=(int) (newlocation.getAccuracy()-currentBestLocation.getAccuracy());
		boolean isLessAccurate=accuracyDelta>0;
		boolean isMoreAccurate=accuracyDelta<0;
		boolean isSignificantlyLessAccurate=accuracyDelta>200;
		
		  // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newlocation.getProvider(),
                currentBestLocation.getProvider());
        
		if(isMoreAccurate){
			return newlocation;
		}
		else if(isNewer && !isLessAccurate){
			return newlocation;
		}
		else if(isNewer && !isSignificantlyLessAccurate && isFromSameProvider){
			return newlocation;
		}
		return currentBestLocation;
		
		
		
		
	}
	
	/** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }
    
    // AsyncTask encapsulating the reverse-geocoding API.  Since the geocoder API is blocked,
    // we do not want to invoke it from the UI thread.
	private class ReverseGeocodingTask extends AsyncTask<Location, Void, Void>{

		Context mContext;
		
		public ReverseGeocodingTask(Context context) {
			super();
			mContext=context;
		}

		@Override
		protected Void doInBackground(Location... params) {
			Geocoder geocoder=new Geocoder(mContext, Locale.getDefault());
			
			Location loc=params[0];
			List<Address> addresses=null;
			
			try {
				addresses=geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                // Update address field with the exception.
				Message.obtain(mHandler, UPDATE_ADDRESS, e.toString()).sendToTarget();
			}
			
			if(addresses !=null && addresses.size()>0){
				Address address=addresses.get(0);
			       // Format the first line of address (if available), city, and country name.
                String addressText = String.format("%s, %s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName());
                // Update address field on UI.
                Message.obtain(mHandler, UPDATE_ADDRESS, addressText).sendToTarget();
				
			}
			

			return null;
		}
		
	}
	
	
		
	


}
