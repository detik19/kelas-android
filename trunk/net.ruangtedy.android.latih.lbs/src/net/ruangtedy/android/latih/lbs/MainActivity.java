package net.ruangtedy.android.latih.lbs;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/*
 * tes tes
 */
public class MainActivity extends Activity {
	
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES=1;
	private static final long MINIMUM_TIME_BETWEEN_UPDATES=1000;

	Button mLocationButton;
	LocationManager mLocationManager;
	
	@Override     
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLocationButton = (Button) findViewById(R.id.button1);
		
		mLocationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
		
		mLocationButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCurrentLocation();
			}
		});
		
	}
	
	private void showCurrentLocation(){
		Location location= mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if(location!=null){
			String msg=String.format("Current Location n Longitude: %1$s n Latitude: %2$s",location.getLongitude(), location.getLatitude());
			Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			String msg=String.format("New Location n Longitude %1$s n Latitude %2$s", location.getLongitude(), location.getLatitude());
			Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onProviderDisabled(String provider) {

			Toast.makeText(MainActivity.this, "Provider disabled by the user, GPS Turn off",Toast.LENGTH_LONG).show();			
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(MainActivity.this, "Provider enabled by the user, GPS Turn on",Toast.LENGTH_LONG).show();			
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Toast.makeText(MainActivity.this, "Provider status changed",Toast.LENGTH_LONG).show();			
		}
		
	}

}
