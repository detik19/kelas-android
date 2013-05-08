package net.ruangtedy.android.latih.intents.implicit;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class CallIntentsActivity extends Activity implements OnClickListener{

	private Spinner spinner;
	ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		spinner=(Spinner) findViewById(R.id.spinner1);
		ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.intents, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		Button btnIntent=(Button) findViewById(R.id.button1);
		mImageView=(ImageView) findViewById(R.id.imageView1);
		btnIntent.setOnClickListener(this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.call_intents, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		int position = spinner.getSelectedItemPosition();
		Intent intent=null;
		switch(position){
			case 0:
				intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://java.ruangtedy.net"));
				startActivity(intent);
				break;
			case 1:
				intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:+6285654069756"));
				startActivity(intent);
				break;
			case 2:
				intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+6285654069756"));
				startActivity(intent);
				break;
			case 3:
				intent=new Intent(Intent.ACTION_VIEW, Uri.parse("geo:50.123,7.1434?z=19"));
				startActivity(intent);
				break;
			case 4:
				 intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=query"));
				 startActivity(intent);
				 break;			
			case 5:
				intent=new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 0);
				break;
			case 6: 
				intent=new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/"));
				startActivity(intent);
				break;
			case 7:
				intent=new Intent(Intent.ACTION_EDIT, Uri.parse("content://contacts/people/l"));
				startActivity(intent);
				break;
				
			case 8:
				intent= new Intent(Intent.ACTION_PICK);
				intent.setType("images/*");
				startActivityForResult(intent, 1);
				
		}
	
		
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 0) {
			Bitmap cameraPic=(Bitmap) data.getExtras().get("data");
			mImageView.setImageBitmap(cameraPic);
		 }
		else if(resultCode==Activity.RESULT_OK&&requestCode==1){
			 Uri photoUri = data.getData();
   		  	mImageView.setImageURI(photoUri);
		}
	}

}
