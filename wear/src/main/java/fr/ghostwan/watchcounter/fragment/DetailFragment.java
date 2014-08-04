package fr.ghostwan.watchcounter.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.ghostwan.watchcounter.MainActivity;
import fr.ghostwan.watchcounter.R;

/**
 * Created by erwan on 04/08/2014.
 */
public class DetailFragment extends AbstractFragment {

	private MainActivity activity;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = (MainActivity)getActivity();
		return  inflater.inflate(R.layout.details_layout, container, false);
	}

	@Override
	public void refresh() {
		((TextView)getView().findViewById(R.id.total_tv)).setText("Total : "+activity.getTotalCounter());
	}
}
