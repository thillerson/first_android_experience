package com.w2e.firehose.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.w2e.firehose.R;
import com.w2e.firehose.resources.Status;
import com.w2e.firehose.twitter.TwitterAPI;

public class SimpleListWithOnClick extends ListActivity {
	
	final private Handler handler = new Handler();
	final private TwitterAPI api = new TwitterAPI();
	private StatusListAdapter adapter;
	private ProgressDialog progressDialog;
	protected ArrayList<Status> lastResult;
	
	private Runnable finishedLoadingTask = new Runnable() {
		public void run() {
			finishedLoading();
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        adapter = new StatusListAdapter(this);
        setListAdapter(adapter);
        reload();
    }
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Status clickedStatus = lastResult.get(position);
		long personId = clickedStatus.getUser().getId();
		Intent intent = new Intent(this, PersonDetailActivity.class);
		intent.putExtra(PersonDetailActivity.PERSON_ID_EXTRA, personId);
		startActivity(intent);
	}
	
	private void setupViews() {
		setContentView(R.layout.list_with_reload);
		Button reloadButton = (Button)findViewById(R.id.reload_button);
		reloadButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				reload();
			}
		});
	}

	protected void reload() {
		progressDialog = ProgressDialog.show(
				this,
				getResources().getString(R.string.loading_title),
				getResources().getString(R.string.loading_description)
			);
		Thread loadTimelineThread = new Thread() {
			public void run() {
				lastResult = api.getPublicTimeline();
				handler.post(finishedLoadingTask);
			}
		};
		loadTimelineThread.start();
	}
	
	private void finishedLoading() {
		adapter.clear();
		for (Status status : lastResult) {
			adapter.add(status);
		}
		progressDialog.dismiss();
	}
	
	private class StatusListAdapter extends ArrayAdapter<Status> {

		public StatusListAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1);
		}
		
	}
}
