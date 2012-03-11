package st.geekli.android;

import st.geekli.android.activities.AuthActivity;
import st.geekli.android.fragments.ActivityFeedFragment;
import st.geekli.android.fragments.TrendingUserFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

public class MainActivity extends SherlockActivity {
  private ActionBar       bar;
  private FragmentManager fragmentManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (!Configuration.isAuth(this)) {
      startActivity(new Intent(this, AuthActivity.class));
    }

    bar = getSupportActionBar();
    bar.setTitle(R.string.app_name);
    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    bar.setDisplayShowTitleEnabled(true);
    bar.addTab(bar.newTab()
                  .setText(R.string.tab_activity_feed)
                  .setTabListener(new TabListener<ActivityFeedFragment>(this,
                                                                        "activityfeed",
                                                                        ActivityFeedFragment.class)));
    bar.addTab(bar.newTab()
                  .setText(R.string.tab_personal_feed)
                  .setTabListener(new TabListener<ActivityFeedFragment>(this,
                                                                        "personalfeed",
                                                                        ActivityFeedFragment.class)));
    bar.addTab(bar.newTab()
                  .setText(R.string.tab_trending_users)
                  .setTabListener(new TabListener<TrendingUserFragment>(this,
                                                                        "trendingusers",
                                                                        TrendingUserFragment.class)));

  }

  public void onResume() {
    super.onResume();
    if (Configuration.isAuth(this)) {
      Api.initApiWithCreds(this);
    }
  }

  /**
   * public boolean onCreateOptionsMenu(Menu menu) { MenuInflater inflater = getMenuInflater();
   * inflater.inflate(R.menu.options_menu, menu); return true; }
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
  }

  public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private final Activity mActivity;
    private final String   mTag;
    private final Class<T> mClass;
    private final Bundle   mArgs;
    private Fragment       mFragment;

    public TabListener(Activity activity, String tag, Class<T> clz) {
      this(activity, tag, clz, null);
    }

    public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
      mActivity = activity;
      mTag = tag;
      mClass = clz;
      mArgs = args;

      // Check to see if we already have a fragment for this tab, probably
      // from a previously saved state.  If so, deactivate it, because our
      // initial state is that a tab isn't shown.
      mFragment = fragmentManager.findFragmentByTag(mTag);
      if (mFragment != null && !mFragment.isDetached()) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.detach(mFragment);
        ft.commit();
      }
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
      if (mFragment == null) {
        mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
        ft.add(android.R.id.content, mFragment, mTag);
      } else {
        ft.attach(mFragment);
      }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
      if (mFragment != null) {
        ft.detach(mFragment);
      }
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
      Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
    }
  }
}
