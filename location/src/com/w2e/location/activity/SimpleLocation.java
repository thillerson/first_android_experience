package com.w2e.location.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.w2e.location.R;

import android.app.Activity;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SimpleLocation extends Activity implements LocationListener {

	private TextView latText;
	private TextView longText;
	private TextView locationText;
	private TextView accuracyText;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViews();
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(c, true);
        locationManager.requestLocationUpdates(provider, 0, 0, this);
    }

	// try 
	// long: -122.402000427
	// lat: 37.784198761
	public void onLocationChanged(Location location) {
		Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();
		latText.setText(String.valueOf(location.getLatitude()));
		longText.setText(String.valueOf(location.getLongitude()));
		accuracyText.setText(String.valueOf(location.getAccuracy()));
		Geocoder geocoder = new Geocoder(this, Locale.US);
		try {
			List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (addresses.size() > 0) {
				Address firstAddress = addresses.get(0);
				Log.d(getClass().getName(), "Found address" + firstAddress.toString());
				locationText.setText(String.format("%s\n%s\n%s\n", firstAddress.getAddressLine(0), firstAddress.getLocality(), firstAddress.getCountryName()));
			} else {
				locationText.setText("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onProviderDisabled(String provider) {
		Log.i(getClass().getName(), String.format("provider '%s' disabled", provider));
	}

	public void onProviderEnabled(String provider) {
		Log.i(getClass().getName(), String.format("provider '%s' disabled", provider));
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i(getClass().getName(), String.format("provider '%s' status changed to %d. Has extras: %b", provider, status, (null != extras)));
	}

	private void findViews() {
		latText = (TextView)findViewById(R.id.lat_text);
		longText = (TextView)findViewById(R.id.long_text);
		accuracyText = (TextView)findViewById(R.id.accuracy_text);
		locationText = (TextView)findViewById(R.id.location_text);
	}

}