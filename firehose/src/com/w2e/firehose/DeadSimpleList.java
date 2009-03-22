package com.w2e.firehose;

import java.util.ArrayList;

import com.w2e.firehose.resources.Status;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class DeadSimpleList extends ListActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAPI api = new TwitterAPI();
        StatusListAdapter adapter = new StatusListAdapter(this, api.getPublicTimeline());
        setListAdapter(adapter);
    }
	
	private class StatusListAdapter extends ArrayAdapter<Status> {

		public StatusListAdapter(Context context, ArrayList<Status> statii) {
			super(context, android.R.layout.simple_list_item_1, statii);
		}
		
	}
}