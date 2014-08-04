package fr.ghostwan.watchcounter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by epinault on 04/08/2014.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragments;

	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new ArrayList<Fragment>();
	}

	@Override
	public Fragment getItem(int i) {
		return fragments.get(i);
	}

	public void addFragment(Fragment fragment) {
		fragments.add(fragment);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return fragments.size();
	}
}
