package fr.ghostwan.watchcounter;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.*;

import java.util.List;

public class DataLayerListenerService extends WearableListenerService {

	private static final String TAG = "DataLayerListenerService";

	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {
		super.onDataChanged(dataEvents);
		SharedPreferences preference = getSharedPreferences(MainActivity.STORE_PREFERENCE, 0);
		SharedPreferences.Editor editor = preference.edit();
		final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
		for (DataEvent event : events) {
			final Uri uri = event.getDataItem().getUri();
			final String path = uri != null ? uri.getPath() : null;

			if ("/PREFERENCE".equals(path)) {
				final DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
				// read your values from map:
				extractInt(MainActivity.PREF_WARNING_INTERVAL, editor, map);
				extractInt(MainActivity.PREF_RESET_INTERVAL, editor, map);
				extractBool(MainActivity.PREF_FULL_RESET, editor, map);
				extractBool(MainActivity.PREF_QUIT_ON_CLICK, editor, map);
			}
		}
		editor.commit();
	}

	public void extractInt(String key, SharedPreferences.Editor editor, DataMap map) {
		if(map.containsKey(key)) {
			int value = map.getInt(key);
			Log.d(TAG, "get "+key+ " : "+value);
			editor.putInt(key, value);
		}
	}
	public void extractBool(String key, SharedPreferences.Editor editor, DataMap map) {
		if(map.containsKey(key)) {
			boolean value = map.getBoolean(key);
			Log.d(TAG, "get "+key+ " : "+value);
			editor.putBoolean(key, value);
		}
	}
}