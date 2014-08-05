package fr.ghostwan.watchcounter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import fr.ghostwan.watchcounter.fragment.CounterFragment;
import fr.ghostwan.watchcounter.fragment.DetailFragment;
import fr.ghostwan.watchcounter.fragment.HelpFragment;
import fr.ghostwan.watchcounter.fragment.ResetFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by epinault on 04/08/2014.
 */
public class MainActivity extends Activity {


	public static final String STORE_VALUE = "fr.ghostwan.watchcounter.value";
	public static final String VALUE_DAY_COUNTER = "VALUE_DAY_COUNTER";
	public static final String VALUE_TOTAL_COUNTER = "VALUE_TOTAL_COUNTER";
	public static final String VALUE_CLICK_TIME = "VALUE_CLICK_TIME";
	public static final String VALUE_FIRST_CLICK_TIME = "VALUE_FIRST_CLICK_TIME";

	public static final String STORE_PREFERENCE = "fr.ghostwan.watchcounter.preference";
	public static final String PREF_WARNING_INTERVAL = "PREF_WARNING_INTERVAL";
	public static final String PREF_RESET_INTERVAL = "PREF_RESET_INTERVAL";
	public static final String PREF_FIRST_START = "PREF_FIRST_START";
	public static final String PREF_FULL_RESET = "PREF_FULL_RESET";
	public static final String PREF_QUIT_ON_CLICK = "PREF_QUIT_ON_CLICK";


	private static final String TAG = "MainActivity";

	private ViewPager mViewPager;

	private int dayCounter = 0;
	private int totalCounter = 0;
	private long clickTime = 0;

	private SharedPreferences valueStorage;
	private SharedPreferences preference;

	public void setFirstClickTime(long firstClickTime) {
		this.firstClickTime = firstClickTime;
	}

	public long getClickTime() {
		return clickTime;
	}

	public void setClickTime(long clickTime) {
		this.clickTime = clickTime;
	}

	public int getTotalCounter() {
		return totalCounter;
	}

	public int getDayCounter() {
		return dayCounter;
	}

	public int getWarningIntervalPref() {
		return preference.getInt(PREF_WARNING_INTERVAL, 0);
	}

	private long firstClickTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		MainPagerAdapter pagerAdapter = new MainPagerAdapter(getFragmentManager());
		mViewPager.setOnPageChangeListener(pagerAdapter);


		valueStorage = getSharedPreferences(STORE_VALUE, 0);
		preference = getSharedPreferences(STORE_PREFERENCE, 0);

		int resetIntervalPref = preference.getInt(PREF_RESET_INTERVAL, 0);

		boolean isFirstStart = true;
		if(preference.getBoolean(PREF_FULL_RESET, false)) {
			totalCounter = 0;
			dayCounter = 0;
			clickTime = 0;
			SharedPreferences.Editor editor = preference.edit();
			editor.putBoolean(PREF_FULL_RESET, false);
			editor.apply();
		}
		else {
			isFirstStart = preference.getBoolean(PREF_FIRST_START, true);
			totalCounter = valueStorage.getInt(VALUE_TOTAL_COUNTER, 0);
			dayCounter = valueStorage.getInt(VALUE_DAY_COUNTER, 0);
			clickTime = valueStorage.getLong(VALUE_CLICK_TIME, 0);

			long time = valueStorage.getLong(VALUE_FIRST_CLICK_TIME, 0);
			if (resetIntervalPref > 0) {

				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(time));
				cal.add(Calendar.DATE, resetIntervalPref);
				Date datePlusInterval = cal.getTime();

				Log.d(TAG, "datePlusInterval : " + DateFormat.getDateTimeInstance().format(datePlusInterval) + "Today :" + DateFormat.getDateTimeInstance().format(new Date()));
				if (time > 100 && datePlusInterval.compareTo(new Date()) <= 0) {
					reset();
				}
			}
		}



		if(isFirstStart) {
			pagerAdapter.addFragment(new HelpFragment());
			SharedPreferences.Editor editor = preference.edit();
			editor.putBoolean(PREF_FIRST_START, false);
			editor.apply();
		}

		pagerAdapter.addFragment(new CounterFragment());
		pagerAdapter.addFragment(new ResetFragment());
		pagerAdapter.addFragment(new DetailFragment());
		mViewPager.setAdapter(pagerAdapter);
	}

	public void goBack() {
		if (mViewPager.getCurrentItem() == 0)
			finish();
		else {
			mViewPager.setCurrentItem(0);
		}
	}

	@Override
	public void onPause() {
		storeValues();
		super.onPause();
	}

	private void storeValues() {
		SharedPreferences.Editor editor = valueStorage.edit();
		editor.putInt(VALUE_DAY_COUNTER, dayCounter);
		editor.putInt(VALUE_TOTAL_COUNTER, totalCounter);
		editor.putLong(VALUE_CLICK_TIME, clickTime);
		editor.putLong(VALUE_FIRST_CLICK_TIME, firstClickTime);
		editor.apply();
	}

	public void addDayCounter() {
		dayCounter++;
		if(preference.getBoolean(PREF_QUIT_ON_CLICK, false)) {
			finish();
		}
	}

	public void reset() {
		totalCounter += dayCounter;
		dayCounter = 0;
		clickTime = 0;
		storeValues();
	}
}
