package st.geekli.android;

import st.geekli.android.activities.AuthActivity;
import st.geekli.android.fragments.ActivityFeedFragment;
import st.geekli.android.fragments.ProfileUserFragment;
import st.geekli.android.fragments.TrendingUserFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

public class MainActivity extends SherlockFragmentActivity {
  private ActionBar actionBar;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    if (!Configuration.isAuth(this)) {
      startActivity(new Intent(this, AuthActivity.class));
    }

    actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    actionBar.setTitle(R.string.app_name);

    actionBar.addTab(actionBar.newTab()
                              .setText(getString(R.string.tab_activity_feed))
                              .setTabListener(new GeekTabListener(new ActivityFeedFragment())));
    actionBar.addTab(actionBar.newTab()
                              .setText(getString(R.string.tab_personal_feed))
                              .setTabListener(new GeekTabListener(new ActivityFeedFragment())));
    actionBar.addTab(actionBar.newTab()
                              .setText(getString(R.string.tab_trending_users))
                              .setTabListener(new GeekTabListener(new TrendingUserFragment())));
    actionBar.addTab(actionBar.newTab()
                              .setText(getString(R.string.tab_profile))
                              .setTabListener(new GeekTabListener(new ProfileUserFragment())));
  }

  public void onResume() {
    super.onResume();
    if (Configuration.isAuth(this)) {
      Api.initApiWithCreds(this);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getSupportMenuInflater().inflate(R.menu.options_menu, menu);
    return true;
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
  }

  public class GeekTabListener implements TabListener {
    private Fragment mFragment;

    public GeekTabListener(Fragment fragment) {
      mFragment = fragment;
    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction ft) {
      getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
      if (mFragment != null) {
        getSupportFragmentManager().beginTransaction().add(R.id.tabcontent, mFragment, "").commit();
      }
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {}
  }
}
