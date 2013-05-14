package net.ruangtedy.android.latih.listview;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class ListViewDemoActivity extends ListActivity {
	String[] words = new String[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		int images[]={R.drawable.ic_launcher,R.drawable.ic_launcher};
		 words[0] = "Bandung";
	     words[1] = "Jakarta";
	        ListImageAdapter adapter = new ListImageAdapter(this, images, words);
	        setListAdapter(adapter);    
	     
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, DetailListActivity.class);
    	intent.putExtra("COUNTRY", words[position]);
    	intent.putExtra("CAPITAL", words[position]);
    	intent.putExtra("DESC", words[position]);
    	startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_view_demo, menu);
		return true;
	}

}
