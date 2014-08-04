package fr.ghostwan.watchcounter;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.GridViewPager;

/**
 * Created by epinault on 04/08/2014.
 */
public class MainActivity extends Activity {


	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final Resources res = getResources();
		mViewPager = (ViewPager) findViewById(R.id.pager);
		MainPagerAdapter pagerAdapter = new MainPagerAdapter(getFragmentManager());

		pagerAdapter.addFragment(new CounterFragment());
		pagerAdapter.addFragment(new ResetFragment());
		mViewPager.setAdapter(pagerAdapter);
	}
}
