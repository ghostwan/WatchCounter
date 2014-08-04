package fr.ghostwan.watchcounter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.ghostwan.watchcounter.R;

/**
 * Created by erwan on 04/08/2014.
 */
public class HelpFragment extends AbstractFragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return  inflater.inflate(R.layout.start_layout, container, false);
	}

	@Override
	public void refresh() {

	}
}
