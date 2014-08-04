package fr.ghostwan.watchcounter.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.ghostwan.watchcounter.MainActivity;
import fr.ghostwan.watchcounter.R;

import java.text.DateFormat;
import java.util.Date;


/**
 * Created by epinault on 04/08/2014.
 */
public class CounterFragment extends AbstractFragment {

	private TextView timeCounterTV;
	private TextView dayCounterTV;
	private RelativeLayout mainLayout;

	private MainActivity activity;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.counter_layout, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		dayCounterTV = (TextView) getView().findViewById(R.id.day_counter);
		timeCounterTV = (TextView) getView().findViewById(R.id.time_counter);
		mainLayout = (RelativeLayout) getView().findViewById(R.id.main_layout);

		activity = (MainActivity)getActivity();

		dayCounterTV.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				activity.setClickTime(new Date().getTime());
				if (activity.getDayCounter() == 0)
					activity.setFirstClickTime(activity.getClickTime());
				activity.addDayCounter();
				((TextView) v).setText("" + activity.getDayCounter());
				timeCounterTV.setText("0s");
				return false;
			}
		});
	}

	@Override
	public void onStart() {
		refresh();
		super.onStart();
	}

	@Override
	public void refresh() {
		if (activity.getClickTime() != 0) {

			String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

			long diff = new Date().getTime() - activity.getClickTime();

			if (activity.getWarningIntervalPref() > 0) {
				if (diff < (60 * activity.getWarningIntervalPref() * 1000))
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
			mainLayout.setBackgroundColor(Color.parseColor("#000000"));
			timeCounterTV.setText("0s");
		}
		dayCounterTV.setText("" + activity.getDayCounter());
	}
}
