package net.ruangtedy.android.latih.gmapv2;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

public class GMapV2Activity extends FragmentActivity {

	private GoogleMap mGmaps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		FragmentManager myFragmentManager=getSupportFragmentManager();
		SupportMapFragment mySupportMapFragment= (SupportMapFragment) myFragmentManager.findFragmentById(R.id.map);
		mGmaps=mySupportMapFragment.getMap();
		
		//my location
		mGmaps.setMyLocationEnabled(true);
	
		//mGmaps.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		//mGmaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);\
		//mGmaps.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		mGmaps.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
