package net.ruangtedy.android.latih.gmapv2;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

public class GMapV2Activity extends FragmentActivity implements OnMapClickListener, OnMapLongClickListener  {

	private GoogleMap mGmaps;
	TextView mTextLocInfo;
	private static final LatLng sttn_batan = new LatLng(-7.775649,110.415382);//,110.415382
	private static final LatLng mirota_kampus = new LatLng(-7.783188, 110.414562);//-7.783188,110.414562
	private static final LatLng bandara_adisucipto = new LatLng(-7.788052,110.431785);//-7.788052,110.431785
	//private static final LatLng Embankment = new LatLng(51.507, -0.122);
	//private static final LatLng Charing_Cross = new LatLng(51.5073, -0.12755);
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		FragmentManager myFragmentManager=getSupportFragmentManager();
		SupportMapFragment mySupportMapFragment= (SupportMapFragment) myFragmentManager.findFragmentById(R.id.map);
		mGmaps=mySupportMapFragment.getMap();
		mTextLocInfo=(TextView) findViewById(R.id.locinfo);
		
		//my location
		mGmaps.setMyLocationEnabled(true);		
		//mGmaps.setTrafficEnabled(true);

		//mGmaps.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		mGmaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		//mGmaps.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		//mGmaps.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		
		mGmaps.addMarker(new MarkerOptions().position(sttn_batan).title("STTN - BATAN"));
		mGmaps.addMarker(new MarkerOptions().position(mirota_kampus).title("Mirota Kampus  Babarsari"));
		mGmaps.addMarker(new MarkerOptions().position(bandara_adisucipto).title("Dari sini"));
		//mGmaps.addMarker(new MarkerOptions().position(Embankment).title("Embankment"));
		//mGmaps.addMarker(new MarkerOptions().position(Charing_Cross).title("Charing Cross"));
		
		mGmaps.setOnMapClickListener(this);
		mGmaps.setOnMapLongClickListener(this);
		
		final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                	LatLngBounds bounds = new LatLngBounds.Builder()
                	.include(sttn_batan)
                	.include(bandara_adisucipto)
                	.include(mirota_kampus)
                	//.include(Embankment)
                	//.include(Charing_Cross)
                	.build();
                	
                	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                		mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);	
                	} else {
                		mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);	
                	}
                	mGmaps.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }});    
        }

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onMapLongClick(LatLng point) {
		mTextLocInfo.setText(point.toString());
		mGmaps.animateCamera(CameraUpdateFactory.newLatLng(point));		
	}

	@Override
	public void onMapClick(LatLng point) {
		mTextLocInfo.setText("New marker added@" + point.toString());
		mGmaps.addMarker(new MarkerOptions().position(point).title(point.toString()));		
	}

}
