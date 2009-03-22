package com.w2e.firehose;

import java.util.ArrayList;

import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import com.w2e.firehose.resources.Status;
import com.w2e.firehose.resources.User;

public class TwitterAPI {
	
	public ArrayList<Status> getPublicTimeline() {
		try {
			return Status.listFromJSON(get("http://twitter.com/statuses/public_timeline.json"));
		} catch (Exception e) {
			Log.e(getClass().getName(), "Error getting public timeline:", e);
		}
		return null;
	}

	public User getUser(long id) {
		try {
			return User.fromJSON(get(String.format("http://twitter.com/users/%d.json", id)));
		} catch (Exception e) {
			Log.e(getClass().getName(), "Error getting user:", e);
		}
		return null;
	}

	private String get(String url) throws Exception {
		HttpGet get = new HttpGet(url);
		return execute(get);
	}

	private String execute(HttpRequestBase method) throws Exception {
		HttpContext localContext = new BasicHttpContext();
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		CookieStore cookieStore = new BasicCookieStore();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpProtocolParams.setContentCharset(httpClient.getParams(), "UTF-8");
		HttpProtocolParams.setHttpElementCharset(httpClient.getParams(), "UTF-8");
		
		String response = httpClient.execute(method, responseHandler, localContext);
		return response;
	}

}
