package net.ruangtedy.android.latih.listview;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		TextView textView = (TextView) findViewById(R.id.textView1);
        String capital = this.getIntent().getExtras().getString("CAPITAL");
        String country = this.getIntent().getExtras().getString("COUNTRY");
        textView.setText(capital + " of " + country);
        
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        String desc = this.getIntent().getExtras().getString("DESC");
        textView2.setText(desc);
        
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_list, menu);
		return true;
	}

}
