package net.ruangtedy.android.latih.json;

import java.util.List;
import java.util.zip.Inflater;

import net.ruangtedy.android.latih.json.imageloader.ImageLoader;
import net.ruangtedy.android.latih.json.model.Twitter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter{

	private Activity mActivity;
	private List<Twitter> mList;
	private static LayoutInflater mInflater;
	public CustomAdapter(Activity a, List<Twitter> list) {
		this.mActivity = a;
		mList = list;
		mInflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
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
		//vi=mInflater.inflate(android.R.layout.simple_list_item_2, null);

		
		TextView view=(TextView) vi.findViewById(R.id.text1);
		view.setText(mList.get(position).getFrom_user_name());
		
		TextView view2=(TextView) vi.findViewById(R.id.text2);
		view2.setText(mList.get(position).getText());
		
		ImageView imageView=(ImageView) vi.findViewById(R.id.imageView1);
		ImageLoader loader = new ImageLoader(mActivity.getApplicationContext());
		loader.DisplayImage(mList.get(position).getProfile_image_url(), imageView);

		// TODO Auto-generated method stub
		return vi;
	}

}
