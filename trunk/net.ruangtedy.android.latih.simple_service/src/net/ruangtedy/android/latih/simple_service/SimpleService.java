package net.ruangtedy.android.latih.simple_service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class SimpleService extends Service {

	boolean mPaused=false;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();
		mPaused=false;
		Thread initBkgThread =new Thread(new Runnable() {
			
			@Override
			public void run() {
				play_music();
			}
		});
		initBkgThread.start();
		
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(this, "service destroyed..", Toast.LENGTH_LONG).show();
		mPaused=true;
		mMediaPlayer.stop();
	}
	
	int notes=R.raw.rd;
	int NOTE_DURATION=400;
	MediaPlayer mMediaPlayer;
	private void play_music(){
		
		// check to ensure main activity not paused
		if (!mPaused) {
			if (mMediaPlayer != null) {
				mMediaPlayer.release();

			}
			mMediaPlayer = MediaPlayer.create(this, notes);
			mMediaPlayer.start();

		}
		
	}

}
