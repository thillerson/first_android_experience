package com.w2e.firehose.tasks;

import java.util.ArrayList;

import com.w2e.firehose.twitter.TwitterAPI;

public class GetPublicTimelineTask extends UserTask<Void, Void, ArrayList<com.w2e.firehose.resources.Status>> {

	public interface GetPublicTimelineResponder {
		public void loadingTimeline();
		public void timelineLoaded(ArrayList<com.w2e.firehose.resources.Status> statii);
	}

	private GetPublicTimelineResponder responder;
	
	public GetPublicTimelineTask(GetPublicTimelineResponder responder) {
		this.responder = responder;
	}

	@Override
	public ArrayList<com.w2e.firehose.resources.Status> doInBackground(Void... params) {
		TwitterAPI api = new TwitterAPI();
		return api.getPublicTimeline();
	}

	@Override
	public void onPreExecute() {
		responder.loadingTimeline();
	}
	
	@Override
	public final void onPostExecute(ArrayList<com.w2e.firehose.resources.Status> statii) {
		responder.timelineLoaded(statii);
	}
}
