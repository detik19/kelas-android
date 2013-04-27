package net.ruangtedy.android.latih.gcm.airbop;

import static net.ruangtedy.android.latih.gcm.airbop.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static net.ruangtedy.android.latih.gcm.airbop.CommonUtilities.EXTRA_MESSAGE;
import static net.ruangtedy.android.latih.gcm.airbop.CommonUtilities.USE_LOCATION;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DemoActivity extends AirBopActivity {

	 TextView mDisplay;
	    
	    //private static final String TAG = "DemoActivity";
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	                
	        setContentView(R.layout.main);
	        mDisplay = (TextView) findViewById(R.id.display);
	        registerReceiver(mHandleMessageReceiver,
	                new IntentFilter(DISPLAY_MESSAGE_ACTION));
	       
	        // Call the register function in the AirBopActivity 
	        register(USE_LOCATION);  
	    }

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.options_menu, menu);
	        return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch(item.getItemId()) {
	            
	            case R.id.options_register:
	            	register(USE_LOCATION);
	                return true;
	            case R.id.options_unregister:
	            	unRegister();
	                return true;
	            case R.id.options_clear:
	                mDisplay.setText(null);
	                return true;
	            case R.id.options_exit:
	                finish();
	                return true;
	           /*
	            case R.id.options_unregister_gcm:
	            	GCMRegistrar.unregister(getApplicationContext());
	            	GCMRegistrar.setRegisteredOnServer(getApplicationContext(), false);
	                return true;  
	            case R.id.options_unregister_airbop:
	            	unRegister(false);
	                return true;  
	            */
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }

	    @Override
	    protected void onDestroy() {
	        
	        unregisterReceiver(mHandleMessageReceiver);
	        super.onDestroy();
	    }

	    
	    private final BroadcastReceiver mHandleMessageReceiver =
	            new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
	            mDisplay.append(newMessage + "\n");
	        }
	    };

}
