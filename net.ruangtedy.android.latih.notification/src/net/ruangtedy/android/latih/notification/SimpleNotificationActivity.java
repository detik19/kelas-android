package net.ruangtedy.android.latih.notification;

import android.R.color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class SimpleNotificationActivity extends Activity {

	public static final String NOTIFY_KEY_1="NOTIFY_KEY_1";
	private static final int NOTIFY_1=0x1001;
	private static final int NOTIFY_2=0x1002;
	private static final int NOTIFY_3=0x1003;
	private static final int NOTIFY_4=0x1004;
	private static final int NOTIFY_5=0x1005;
	private NotificationManager notifier = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		notifier=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		final Notification notify=new Notification(R.drawable.ic_launcher, "Hello", System.currentTimeMillis());
		
		notify.icon=R.drawable.ic_launcher;
		notify.tickerText="Hello";
		notify.when=System.currentTimeMillis();
		Button notify1=(Button) findViewById(R.id.notify1);
		notify1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				notify.number++;
				notify.flags|=Notification.FLAG_AUTO_CANCEL;
				Intent toLaunch=new Intent(SimpleNotificationActivity.this, SimpleNotificationActivity.class);
				PendingIntent intentBack=PendingIntent.getActivity(SimpleNotificationActivity.this, 0, toLaunch, 0);
				notify.setLatestEventInfo(SimpleNotificationActivity.this, "Hi There", "this is even more text", intentBack);
				notifier.notify(NOTIFY_1,notify);
				
			}
		});
		
		Button notify2= (Button) findViewById(R.id.notify2);
		notify2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Notification notify =new Notification(android.R.drawable.stat_notify_chat, "vibrate", System.currentTimeMillis());
				notify.flags |= Notification.FLAG_AUTO_CANCEL;
				notify.vibrate=new long[]{ 200, 200, 600, 600, 600, 200,
                        200, 600, 600, 200, 200, 200, 200, 600, 200, 200, 600,
                        200, 200, 600, 600, 200, 600, 200, 600, 600, 200, 200,
                        200, 600, 600, 200, 200, 200, 200, 600, };
				Intent toLaunch=new Intent(SimpleNotificationActivity.this, SimpleNotificationActivity.class);
				PendingIntent intentBack=PendingIntent.getActivity(SimpleNotificationActivity.this, 0, toLaunch, 0);
				notify.setLatestEventInfo(SimpleNotificationActivity.this, "Bzz!", "This vibrated your phone", intentBack);
				notifier.notify(NOTIFY_2, notify);
				
				Vibrator vibe=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibe.vibrate(500);
				
			}
		});
		
		Button notify3=(Button) findViewById(R.id.notify3);
		notify3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				notify.flags|=Notification.FLAG_AUTO_CANCEL;
				notify.number++;
				notify.flags|=Notification.FLAG_SHOW_LIGHTS;
				
				if(notify.number<2){
					notify.ledARGB=Color.GREEN;
					notify.ledOnMS=1000;
					notify.ledOffMS=1000;
					
				}
				else if(notify.number<3){
					notify.ledARGB=Color.BLUE;
					notify.ledOnMS=750;
					notify.ledOffMS=750;
				}
				else if(notify.number<4){
					notify.ledARGB=color.white;
					notify.ledOnMS=500;
					notify.ledOffMS=500;
				}
				else {
					notify.ledARGB=Color.RED;
					notify.ledOnMS=50;
					notify.ledOffMS=50;
				}
				
				Intent toLaunch=new Intent(SimpleNotificationActivity.this, SimpleNotificationActivity.class);
				PendingIntent intentBack=PendingIntent.getActivity(SimpleNotificationActivity.this, 0, toLaunch, 0);
				notify.setLatestEventInfo(SimpleNotificationActivity.this, "Bright", "Thos lit up your phone", intentBack);
				notifier.notify(NOTIFY_3,notify);
			}
		});
		
		Button notify4=(Button) findViewById(R.id.notify4);
		notify4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				notify.flags |=Notification.FLAG_AUTO_CANCEL;
				notify.audioStreamType=AudioManager.STREAM_NOTIFICATION;
				notify.sound=Uri.parse("android.resource://net.ruangtedy.android.latih.notification/"+ + R.raw.fallbackring);
				Intent toLaunch=new Intent(SimpleNotificationActivity.this, SimpleNotificationActivity.class);
				PendingIntent intentBack=PendingIntent.getActivity(SimpleNotificationActivity.this, 0, toLaunch, 0);
				notify.setLatestEventInfo(SimpleNotificationActivity.this, "Wow!!", "This made your phone noisy.", intentBack);
				notifier.notify(NOTIFY_4, notify);
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
