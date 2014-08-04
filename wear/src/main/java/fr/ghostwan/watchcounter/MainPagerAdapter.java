package fr.ghostwan.watchcounter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import fr.ghostwan.watchcounter.fragment.AbstractFragment;

import java.util.ArrayList;

/**
 * Created by epinault on 04/08/2014.
 */
public class MainPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

	private static final String TAG = "MainPagerAdapter";
	private ArrayList<AbstractFragment> fragments;

	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new ArrayList<AbstractFragment>();
	}

	@Override
	public AbstractFragment getItem(int i) {
		return fragments.get(i);
	}

	public void addFragment(AbstractFragment fragment) {
		fragments.add(fragment);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {
	}

	@Override
	public void onPageSelected(int i) {
		getItem(i).refresh();
	}

	@Override
	public void onPageScrollStateChanged(int i) {

	}
}
