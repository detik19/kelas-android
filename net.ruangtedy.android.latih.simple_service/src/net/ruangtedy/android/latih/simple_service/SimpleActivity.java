package net.ruangtedy.android.latih.simple_service;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class SimpleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Button startButton= (Button) findViewById(R.id.button1);
		startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startService(new Intent(SimpleActivity.this, SimpleService.class));
				
			}
		});
		
		Button stopButton=(Button) findViewById(R.id.button2);
		stopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopService(new Intent(SimpleActivity.this, SimpleService.class));
			}
		});
		
		Configuration c = new Configuration(getResources().getConfiguration());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
