package fr.ghostwan.watchcounter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CounterActivity extends Activity {

	private static final String STORE_VALUE = "fr.ghostwan.watchcounter.value";
	private static final String VALUE_DAY_COUNTER = "VALUE_DAY_COUNTER";
	private static final String VALUE_TOTAL_COUNTER = "VALUE_TOTAL_COUNTER";
	private static final String VALUE_CLICK_TIME = "VALUE_CLICK_TIME";
	private static final String VALUE_FIRST_CLICK_TIME = "VALUE_FIRST_CLICK_TIME";

	public static final String STORE_PREFERENCE = "fr.ghostwan.watchcounter.preference";
	public static final String PREF_WARNING_INTERVAL = "PREF_WARNING_INTERVAL";
	public static final String PREF_RESET_INTERVAL = "PREF_RESET_INTERVAL";
	private static final String TAG = "CounterActivity";

	private int dayCounter = 0;
	private int totalCounter = 0;
	private long clickTime = 0;
	private long firstClickTime = 0;

	private SharedPreferences valueStorage;
	private TextView timeCounterTV;
	private TextView dayCounterTV;
	private RelativeLayout mainLayout;

	private int warningIntervalPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_counter);

		valueStorage = getSharedPreferences(STORE_VALUE, 0);

		SharedPreferences preference = getSharedPreferences(STORE_PREFERENCE, 0);
		warningIntervalPref = preference.getInt(PREF_WARNING_INTERVAL, 0);
		int resetIntervalPref = preference.getInt(PREF_RESET_INTERVAL, 0);

		long time = valueStorage.getLong(VALUE_FIRST_CLICK_TIME, 0);
		totalCounter = valueStorage.getInt(VALUE_TOTAL_COUNTER, 0);
		dayCounter = valueStorage.getInt(VALUE_DAY_COUNTER, 0);
		clickTime = valueStorage.getLong(VALUE_CLICK_TIME, 0);

		if (resetIntervalPref > 0) {

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(time));
			cal.add(Calendar.DATE, resetIntervalPref);
			Date datePlusInterval = cal.getTime();

			Log.d(TAG, "datePlusInterval : "+ DateFormat.getDateTimeInstance().format(datePlusInterval)+ "Today :"+ DateFormat.getDateTimeInstance().format(new Date()));
			if (time > 100 && datePlusInterval.compareTo(new Date()) > 0) {
				totalCounter += dayCounter;
				dayCounter = 0;
				clickTime = 0;
				storeValues();
				Log.d("CountActivity", "Reset pref");
			}
		}

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {

				dayCounterTV = (TextView) stub.findViewById(R.id.day_counter);
				timeCounterTV = (TextView) stub.findViewById(R.id.time_counter);
				mainLayout = (RelativeLayout) stub.findViewById(R.id.main_layout);

				updateTime();

				dayCounterTV.setText("" + dayCounter);
				dayCounterTV.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						clickTime = new Date().getTime();
						if (dayCounter == 0)
							firstClickTime = clickTime;
						dayCounter++;
						((TextView) v).setText("" + dayCounter);
						timeCounterTV.setText("0s");
						return false;
					}
				});
			}
		});
	}

	@Override
	protected void onResume() {
		updateTime();
		super.onResume();
	}


	private void updateTime() {
		if (timeCounterTV != null) {

			if (clickTime != 0) {

				String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

				long diff = new Date().getTime() - clickTime;

				if (warningIntervalPref > 0) {
					if (diff < (60 * warningIntervalPref * 1000))
						mainLayout.setBackgroundColor(Color.parseColor("#83241a"));
					else
						mainLayout.setBackgroundColor(Color.parseColor("#386113"));
				}

				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;
				long diffDays = diff / (24 * 60 * 60 * 1000);

				String timeString = "";
				if (diffDays > 0)
					timeString += diffDays + "D ";
				if (diffHours > 0)
					timeString += diffHours + "H ";
				if (diffMinutes > 0)
					timeString += diffMinutes + "m ";
				if (diffSeconds > 0 && !(diffHours > 0 || diffDays > 0))
					timeString += diffSeconds + "s";

				timeCounterTV.setText(timeString);

			} else {
				timeCounterTV.setText("0s");
			}
		}
	}

	private void getValues() {

	}

	private void storeValues() {
		SharedPreferences.Editor editor = valueStorage.edit();
		editor.putInt(VALUE_DAY_COUNTER, dayCounter);
		editor.putInt(VALUE_TOTAL_COUNTER, totalCounter);
		editor.putLong(VALUE_CLICK_TIME, clickTime);
		editor.putLong(VALUE_FIRST_CLICK_TIME, firstClickTime);
		editor.commit();
	}

	@Override
	protected void onPause() {
		storeValues();
		super.onPause();
	}
}
