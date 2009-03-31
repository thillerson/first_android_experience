package com.w2e.firehose.activity;

import java.util.ArrayList;

import com.w2e.firehose.R;
import com.w2e.firehose.resources.Status;
import com.w2e.firehose.tasks.GetPublicTimelineTask;
import com.w2e.firehose.tasks.GetPublicTimelineTask.GetPublicTimelineResponder;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class SimpleListUsingBackroundTask extends ListActivity implements GetPublicTimelineResponder {
	
	private StatusListAdapter adapter;
	private ProgressDialog progressDialog;
	protected ArrayList<Status> lastResult;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        adapter = new StatusListAdapter(this);
        setListAdapter(adapter);
        reload();
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
		new GetPublicTimelineTask(this).execute();
	}
	
	private void finishedLoading() {
		adapter.clear();
		for (Status status : lastResult) {
			adapter.add(status);
		}
		progressDialog.dismiss();
	}
	
	public void loadingTimeline() {
		progressDialog = ProgressDialog.show(
				this,
				getResources().getString(R.string.loading_title),
				getResources().getString(R.string.loading_description)
			);
	}

	public void timelineLoaded(ArrayList<Status> statii) {
		lastResult = statii;
		finishedLoading();
	}

	private class StatusListAdapter extends ArrayAdapter<Status> {

		public StatusListAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1);
		}
		
	}

}
