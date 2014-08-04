package fr.ghostwan.watchcounter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;


public class SettingsActivity extends Activity {

	private static final String TAG = "SettingsActivity";

	public static final String STORE_PREFERENCE = "fr.ghostwan.watchcounter.preference";
	private static final String PREF_WARNING_INTERVAL = "PREF_WARNING_INTERVAL";
	private static final String PREF_RESET_INTERVAL = "PREF_RESET_INTERVAL";
	public static final String PREF_FULL_RESET = "PREF_FULL_RESET";

	private GoogleApiClient mGoogleApiClient;
	private SharedPreferences preference;


	private TextView warningIntervalTextView;
	private TextView resetIntervalTextView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		preference = getSharedPreferences(STORE_PREFERENCE, 0);

		warningIntervalTextView = (TextView) findViewById(R.id.warning_interval_tv);
		Switch warningIntervalSwitch = (Switch) findViewById(R.id.warning_interval_switch);
		int warningInterval = preference.getInt(PREF_WARNING_INTERVAL, 0);

		if(warningInterval > 0) {
			warningIntervalSwitch.setChecked(true);
			warningIntervalTextView.setVisibility(View.VISIBLE);
			warningIntervalTextView.setText(""+ warningInterval);
		}
		warningIntervalSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean on = ((Switch) view).isChecked();

				if (on) {
					warningIntervalTextView.setVisibility(View.VISIBLE);
				} else {
					warningIntervalTextView.setVisibility(View.INVISIBLE);
				}
				warningIntervalTextView.setText("");
			}
		});


		resetIntervalTextView = (TextView) findViewById(R.id.reset_interval_tv);
		Switch resetIntervalSwitch = (Switch) findViewById(R.id.reset_interval_switch);
		int resetInterval = preference.getInt(PREF_RESET_INTERVAL, 0);

		if(resetInterval > 0) {
			resetIntervalSwitch.setChecked(true);
			resetIntervalTextView.setVisibility(View.VISIBLE);
			resetIntervalTextView.setText(""+ resetInterval);
		}
		resetIntervalSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean on = ((Switch) view).isChecked();

				if (on) {
					resetIntervalTextView.setVisibility(View.VISIBLE);
				} else {
					resetIntervalTextView.setVisibility(View.INVISIBLE);
				}
				resetIntervalTextView.setText("");
			}
		});

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
					@Override
					public void onConnected(Bundle connectionHint) {
					}

					@Override
					public void onConnectionSuspended(int cause) {
					}
				})
				.addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
					@Override
					public void onConnectionFailed(ConnectionResult result) {
					}
				})
				.addApi(Wearable.API)
				.build();
		mGoogleApiClient.connect();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public void onSaveButtonClicked(View view) {

		if(mGoogleApiClient==null) {
			Toast.makeText(this, "Connection with watch failed", Toast.LENGTH_SHORT).show();
			return;
		}
		SharedPreferences.Editor editor = preference.edit();
		PutDataMapRequest putRequest = PutDataMapRequest.create("/PREFERENCE");
		DataMap map = putRequest.getDataMap();


		saveInt(PREF_WARNING_INTERVAL, parseTextViewForInt(warningIntervalTextView, 0), editor, map);
		saveInt(PREF_RESET_INTERVAL, parseTextViewForInt(resetIntervalTextView, 0), editor, map);

		editor.commit();
		Wearable.DataApi.putDataItem(mGoogleApiClient, putRequest.asPutDataRequest());
		finish();
	}

	private int parseTextViewForInt(TextView tv, int defaultValue) {
		int value = defaultValue;
		String string = tv.getText().toString();
		if(!string.equals("")) {
			value = Integer.valueOf(string);
		}
		Log.d(TAG, "Value : "+value+ " "+string);
		return value;
	}


	private void saveInt(String key, int value, SharedPreferences.Editor editor, DataMap map) {
		editor.putInt(key, value);
		map.putInt(key, value);
	}

	public void onCancelButtonClicked(View view) {
		finish();
	}

	public void onFullReset(View view) {
		PutDataMapRequest putRequest = PutDataMapRequest.create("/PREFERENCE");
		DataMap map = putRequest.getDataMap();
		map.putBoolean(PREF_FULL_RESET, true);
		Wearable.DataApi.putDataItem(mGoogleApiClient, putRequest.asPutDataRequest());
		finish();
	}
}
