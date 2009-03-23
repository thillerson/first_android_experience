package com.w2e.firehose.activity;

import java.util.ArrayList;

import com.w2e.firehose.R;
import com.w2e.firehose.resources.Status;
import com.w2e.firehose.twitter.TwitterAPI;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class SimpleListUsingThread extends ListActivity {
	
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
        adapter = new StatusListAdapter(this, new ArrayList<Status>());
        setListAdapter(adapter);
        reload();
    }
	
	private void setupViews() {
		setContentView(R.layout.with_reload);
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
				"Loading Public Timelinessssss"
			);
		new Thread() {
			@Override public void run() {
				lastResult = api.getPublicTimeline();
				handler.post(finishedLoadingTask);
			}
		}.start();
	}
	
	private void finishedLoading() {
		adapter.clear();
		for (Status status : lastResult) {
			adapter.add(status);
		}
		progressDialog.dismiss();
	}
	
	private class StatusListAdapter extends ArrayAdapter<Status> {

		public StatusListAdapter(Context context, ArrayList<Status> statii) {
			super(context, android.R.layout.simple_list_item_1, statii);
		}
		
	}
}
