package net.ruangtedy.android.latih.texttospeech;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnInitListener  {

	private TextToSpeech mTTs;
	private Button mBtnSpeak;
	private EditText mTxtText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTTs=new TextToSpeech(this, this);
		mBtnSpeak=(Button) findViewById(R.id.btnSpeak);
		mTxtText=(EditText) findViewById(R.id.txtText);
	
		mBtnSpeak.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				speakOut();
			}
		});
		
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mTTs !=null){
			mTTs.stop();
			mTTs.shutdown();
		}
		super.onDestroy();

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if(status==TextToSpeech.SUCCESS){
			int result=mTTs.setLanguage(Locale.US);
			// tts.setPitch(5); // set pitch level

			// tts.setSpeechRate(2); // set speech speed rate
			if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
				Log.e("TTS", "This Language is not supported");
				
			}else{
				mBtnSpeak.setEnabled(true);
				speakOut();
			}
		}
		
	}
	private void speakOut(){
		String text=mTxtText.getText().toString();
		mTTs.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		
	}

}
