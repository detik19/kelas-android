package net.ruangtedy.android.latih.sms;

import android.os.Bundle;
import android.app.Activity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button mButton;
	EditText mEditPhoneNum;
	EditText mEditSMS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mButton = (Button) findViewById(R.id.buttonSend);
		mEditPhoneNum=(EditText) findViewById(R.id.editPhoneNum);
		mEditSMS=(EditText) findViewById(R.id.editSMS);
		
		mButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String phoneNo=mEditPhoneNum.getText().toString();
				String sms=mEditSMS.getText().toString();
				
				SmsManager smsManager=SmsManager.getDefault();
				smsManager.sendTextMessage(phoneNo, null, sms, null, null);
				Toast.makeText(getApplicationContext(), "SMS Sent", Toast.LENGTH_LONG).show();
				
				
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
