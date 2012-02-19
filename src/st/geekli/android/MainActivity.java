package st.geekli.android;

import st.geekli.android.fragments.ActivityFeedFragment;
import st.geekli.android.fragments.TrendingUserFragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

public class MainActivity extends Activity {
  private ActionBar bar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    bar = getActionBar();
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);
    return true;
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
  }

  public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
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
      mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
      if (mFragment != null && !mFragment.isDetached()) {
        FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
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
