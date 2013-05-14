package net.ruangtedy.android.latih.listview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListImageAdapter extends BaseAdapter {
	private String[] mWords;
	private Activity mActivity;
	private int[] mImages;
	private static LayoutInflater mInflater=null;
	
	public ListImageAdapter(Activity activity, int[] images, String[] words){
		this.mWords=words;
		this.mImages=images;
		this.mActivity=activity;
		mInflater=(LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	@Override
	public int getCount() {
		return mWords.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		vi=mInflater.inflate(R.layout.item, null);
		
		ImageView image=(ImageView) vi.findViewById(R.id.imageView1);
		TextView word= (TextView) vi.findViewById(R.id.textView1);
		
		image.setImageResource(mImages[position]);
		word.setText(mWords[position]);
		
		return vi;
	}
	

}
