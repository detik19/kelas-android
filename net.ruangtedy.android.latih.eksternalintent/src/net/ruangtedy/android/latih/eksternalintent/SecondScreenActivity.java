package net.ruangtedy.android.latih.eksternalintent;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class SecondScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen2);
		TextView txtName=(TextView) findViewById(R.id.txtName);
		TextView txtEmail=(TextView) findViewById(R.id.txtEmail);
	
		Intent i = getIntent();

		// Receiving the Data
        String name = i.getStringExtra("name");
        String email = i.getStringExtra("email");
 
        // Displaying Received data
        txtName.setText(name);
        txtEmail.setText(email);
 
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screen2, menu);
		return true;
	}

}
