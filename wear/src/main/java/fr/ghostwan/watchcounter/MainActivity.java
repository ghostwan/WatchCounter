package fr.ghostwan.watchcounter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import fr.ghostwan.watchcounter.fragment.CounterFragment;
import fr.ghostwan.watchcounter.fragment.DetailFragment;
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
	private static final String TAG = "MainActivity";

	private ViewPager mViewPager;

	private int dayCounter = 0;
	private int totalCounter = 0;
	private long clickTime = 0;
	private int warningIntervalPref;
	private SharedPreferences valueStorage;

	public long getFirstClickTime() {
		return firstClickTime;
	}

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

	public void setTotalCounter(int totalCounter) {
		this.totalCounter = totalCounter;
	}

	public int getDayCounter() {
		return dayCounter;
	}

	public void setDayCounter(int dayCounter) {
		this.dayCounter = dayCounter;
	}

	public int getWarningIntervalPref() {
		return warningIntervalPref;
	}

	public void setWarningIntervalPref(int warningIntervalPref) {
		this.warningIntervalPref = warningIntervalPref;
	}

	private long firstClickTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		MainPagerAdapter pagerAdapter = new MainPagerAdapter(getFragmentManager());
		mViewPager.setOnPageChangeListener(pagerAdapter);

		pagerAdapter.addFragment(new CounterFragment());
		pagerAdapter.addFragment(new ResetFragment());
		pagerAdapter.addFragment(new DetailFragment());
		mViewPager.setAdapter(pagerAdapter);

		valueStorage = getSharedPreferences(STORE_VALUE, 0);

		SharedPreferences preference = getSharedPreferences(MainActivity.STORE_PREFERENCE, 0);
		warningIntervalPref = preference.getInt(MainActivity.PREF_WARNING_INTERVAL, 0);
		int resetIntervalPref = preference.getInt(MainActivity.PREF_RESET_INTERVAL, 0);

		long time = valueStorage.getLong(MainActivity.VALUE_FIRST_CLICK_TIME, 0);
		totalCounter = valueStorage.getInt(MainActivity.VALUE_TOTAL_COUNTER, 0);
		dayCounter = valueStorage.getInt(MainActivity.VALUE_DAY_COUNTER, 0);
		clickTime = valueStorage.getLong(MainActivity.VALUE_CLICK_TIME, 0);

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
		editor.putInt(MainActivity.VALUE_DAY_COUNTER, dayCounter);
		editor.putInt(MainActivity.VALUE_TOTAL_COUNTER, totalCounter);
		editor.putLong(MainActivity.VALUE_CLICK_TIME, clickTime);
		editor.putLong(MainActivity.VALUE_FIRST_CLICK_TIME, firstClickTime);
		editor.apply();
	}

	public void addDayCounter() {
		dayCounter++;
	}

	public void reset() {
		totalCounter += dayCounter;
		dayCounter = 0;
		clickTime = 0;
		storeValues();
	}
}
