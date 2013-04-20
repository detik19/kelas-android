package net.ruangtedy.android.latih.eksternalintent;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstScreenActivity extends Activity {

	EditText mInputName,mInputEmail;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);
        mInputName=(EditText) findViewById(R.id.name);
        mInputEmail=(EditText) findViewById(R.id.email);
        Button btnNextScreen=(Button) findViewById(R.id.NextScreen);
        
        btnNextScreen.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent nextScreen=new Intent(getApplicationContext(), SecondScreenActivity.class);
				 nextScreen.putExtra("name", mInputName.getText().toString());
	                nextScreen.putExtra("email", mInputEmail.getText().toString());
	 
				startActivity(nextScreen);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.screen1, menu);
        return true;
    }
    
}
