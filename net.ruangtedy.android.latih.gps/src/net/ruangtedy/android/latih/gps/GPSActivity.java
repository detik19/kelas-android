package net.ruangtedy.android.latih.gps;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GPSActivity extends Activity {

	TextView tv;
	LocationManager mLocationManager;
	Button mButtonGPS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		
		tv=(TextView) findViewById(R.id.tv);
		Button mButtonGPS=(Button) findViewById(R.id.button1);
		
		mLocationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		mButtonGPS.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {


				getGPS();
			}
		});
		
		
		
	}

	private void getGPS(){
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String locationprovider=mLocationManager.getBestProvider(criteria, true);
		Location mLocation=mLocationManager.getLastKnownLocation(locationprovider);
		tv.setText("Last location lat"+mLocation.getLatitude()+" long:"+mLocation.getLongitude());
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_gps, menu);
		return true;
	}

}
