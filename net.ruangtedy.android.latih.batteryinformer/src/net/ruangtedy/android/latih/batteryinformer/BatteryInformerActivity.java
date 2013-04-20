package net.ruangtedy.android.latih.batteryinformer;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class BatteryInformerActivity extends Activity {

	TextView mTextBatteryLevel=null;
	String mBatteryLevelInfo="Battery Level";
	@Override
	protected void onDestroy() {
		unregisterReceiver(battery_receiver);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mTextBatteryLevel=(TextView) findViewById(R.id.batterylevel_text);
		registerBatteryLevelReceiver();
		
	}
	
	private BroadcastReceiver battery_receiver= new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			boolean isPresent=intent.getBooleanExtra("present", false);
			String technology=intent.getStringExtra("technology");
			int plugged=intent.getIntExtra("plugged", -1);
			int scale=intent.getIntExtra("scale", -1);
			int health=intent.getIntExtra("health", 0);
			int status=intent.getIntExtra("status", 0);
			int rawlevel=intent.getIntExtra("level", -1);
			
			int level=0;
			
			Bundle bundle=intent.getExtras();
			Log.i("BatteryLevel",bundle.toString());
			
			//if(isPresent){
				if(rawlevel>=0 && scale>0){
					level=(rawlevel*100)/scale;	
					
				}
				
				String info="Battery Level: "+level+"%\n";
				
				info +=("Technology: "+technology+"\n");
				info +=("Plugged:"+getPlugTypeString(plugged)+"\n");
				info +=("Health:"+getHealthString(health)+"\n");
				info +=("Status:"+getStatusString(status)+"\n");
				
				setBatteryLevelText(info+ "\n \n "+bundle.toString());
				
		//	}
			//else{
			//	setBatteryLevelText("Battery not present!!");
			//}
			
			
			
		}
	};
	
	private String getPlugTypeString(int plugged){
		String plugType="Unknown";
		
		switch(plugged){
			case BatteryManager.BATTERY_PLUGGED_AC:plugType="AC";break;
			case BatteryManager.BATTERY_PLUGGED_USB:plugType="USB";break;
			
		}
		return plugType;
	}
	
	private String getHealthString(int health){
		String healthString="unknown";
		
		switch(health){
			case BatteryManager.BATTERY_HEALTH_DEAD:healthString="Dead";break;
			case BatteryManager.BATTERY_HEALTH_GOOD:healthString="Good";break;
			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:healthString="Over Voltage";break;
			case BatteryManager.BATTERY_HEALTH_OVERHEAT:healthString="Over heat";break;
			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:healthString="Unspecified failure";break;
			
		}
		return healthString;
				
	}
	
	private String getStatusString(int status){
		String statusString="Unknown";
		switch(status){
			case BatteryManager.BATTERY_STATUS_CHARGING:statusString="Charging";break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:statusString="Discharge";break;
			case BatteryManager.BATTERY_STATUS_FULL:statusString="Full";break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:statusString="Not Charging";break;
			
		}
		return statusString;
	}
	
	private void setBatteryLevelText(String text){
		mTextBatteryLevel.setText(text);
	}
	
	private void registerBatteryLevelReceiver(){
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(battery_receiver, filter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
