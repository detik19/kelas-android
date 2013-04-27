package net.ruangtedy.android.latih.gcm.airbop;

import java.lang.ref.WeakReference;

import com.google.android.gcm.GCMRegistrar;

import android.content.Context;
import android.os.AsyncTask;

public class AirBopRegisterTask extends AsyncTask<Void, Void, Void> {


	private WeakReference<RegTaskCompleteListener> mListener;
	private Context mAppContext = null;
	private AirBopServerUtilities mServerData=null;
	
	
	public AirBopRegisterTask(RegTaskCompleteListener listener
			, final Context appContext
			, final String regId
			, AirBopServerUtilities server_data) {
		
			mAppContext  = appContext;
			//mRegId = regId;
			mServerData = server_data;
			mServerData.mRegId = regId;
			mListener = new WeakReference<RegTaskCompleteListener>(listener);
		}
	@Override
	protected void onPostExecute(Void result) {
		if(mListener!=null){
			RegTaskCompleteListener listener=mListener.get();
			
			if(listener!=null){
				listener.onTaskComplete();
			}
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
	
		boolean registered=AirBopServerUtilities.register(mAppContext, mServerData);
		
		 // At this point all attempts to register with the AirBop
	    // server failed, so we need to unregister the device
	    // from GCM - the app will try to register again when
	    // it is restarted. Note that GCM will send an
	    // unregistered callback upon completion, but
	    // GCMIntentService.onUnregistered() will ignore it.
		if(!registered){
			GCMRegistrar.unregister(mAppContext);
		}
		
		return null;
	}
	
	/**
	* Defines an interface for a callback that will be called
	* when the task is done
	*/
	public interface RegTaskCompleteListener{
		public void onTaskComplete();
	}

}
