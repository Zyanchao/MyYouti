package com.youti.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youti_geren.R;

public class SystemMessageFragment extends Fragment {
	ListView listView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v =View.inflate(getActivity(), R.layout.fragment_systemmessage, null);
		listView =(ListView) v.findViewById(R.id.listview);
		
		TextView tv =new TextView(getActivity());
		tv.setText("系统消息Fragment");
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView.setAdapter(new MyAdapter());
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view ;
			if(convertView==null){
				view=View.inflate(getActivity(), R.layout.item_system_message, null);
			}else{
				view =convertView;
			}
			return view;
		}
		
	}
}
