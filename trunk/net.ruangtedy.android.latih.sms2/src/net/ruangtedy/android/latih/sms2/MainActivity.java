package net.ruangtedy.android.latih.sms2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button mButtonSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mButtonSend =(Button) findViewById(R.id.btnSend);
		mButtonSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent smsIntent=new Intent(Intent.ACTION_VIEW);
				smsIntent.putExtra("sms_body", "JavaCodeGeeks");
				smsIntent.setType("vnd.android-dir/mms-sms");
				startActivity(smsIntent);
				
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
