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
		SharedPreferences preference = getSharedPreferences(CounterActivity.STORE_PREFERENCE, 0);
		SharedPreferences.Editor editor = preference.edit();
		final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
		for (DataEvent event : events) {
			final Uri uri = event.getDataItem().getUri();
			final String path = uri != null ? uri.getPath() : null;

			if ("/PREFERENCE".equals(path)) {
				final DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
				// read your values from map:
				getInt(CounterActivity.PREF_WARNING_INTERVAL, editor, map);
				getInt(CounterActivity.PREF_RESET_INTERVAL, editor, map);
			}
		}
		editor.commit();
	}

	public void getInt(String key, SharedPreferences.Editor editor , DataMap map) {
		int value = map.getInt(key);
		Log.d(TAG, "get Int : "+value);
		editor.putInt(key, value);
	}
}