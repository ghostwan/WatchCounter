package fr.ghostwan.watchcounter.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.ghostwan.watchcounter.MainActivity;
import fr.ghostwan.watchcounter.R;

/**
 * Created by epinault on 04/08/2014.
 */
public class ResetFragment extends AbstractFragment {

	private MainActivity activity;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.reset_layout, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		activity = (MainActivity)getActivity();
		getView().findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				activity.reset();
				((MainActivity)getActivity()).goBack();
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void refresh() {
	}
}
