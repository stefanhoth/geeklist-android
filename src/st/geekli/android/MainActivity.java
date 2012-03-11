package st.geekli.android;

import java.util.HashMap;

import st.geekli.android.activities.AuthActivity;
import st.geekli.android.fragments.ActivityFeedFragment;
import st.geekli.android.fragments.TrendingUserFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class MainActivity extends SherlockFragmentActivity {
  private ActionBar       actionBar;
  private FragmentManager fragmentManager;
  private TabHost         tabHost;
  private TabManager      tabManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    fragmentManager = getSupportFragmentManager();

    tabHost = (TabHost) findViewById(android.R.id.tabhost);
    tabHost.setup();
    tabManager = new TabManager(this, tabHost, R.id.realtabcontent);

    if (!Configuration.isAuth(this)) {
      startActivity(new Intent(this, AuthActivity.class));
    }

    actionBar = getSupportActionBar();
    actionBar.setTitle(R.string.app_name);
    tabManager.addTab(tabHost.newTabSpec("activityfeed").setIndicator(getString(R.string.tab_activity_feed)),
                      ActivityFeedFragment.class,
                      null);
    tabManager.addTab(tabHost.newTabSpec("personalfeed").setIndicator(getString(R.string.tab_personal_feed)),
                      ActivityFeedFragment.class,
                      null);
    tabManager.addTab(tabHost.newTabSpec("trendingusers")
                             .setIndicator(getString(R.string.tab_trending_users)),
                      TrendingUserFragment.class,
                      null);
  }

  public void onResume() {
    super.onResume();
    if (Configuration.isAuth(this)) {
      Api.initApiWithCreds(this);
    }
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getSupportMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);
    return true;
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
  }

  public static class TabManager implements TabHost.OnTabChangeListener {
    private final FragmentActivity         mActivity;
    private final TabHost                  mTabHost;
    private final int                      mContainerId;
    private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    TabInfo                                mLastTab;

    private static final class TabInfo {
      private final String   tag;
      private final Class<?> clss;
      private final Bundle   args;
      private Fragment       fragment;

      public TabInfo(String _tag, Class<?> _class, Bundle _args) {
        tag = _tag;
        clss = _class;
        args = _args;
      }
    }

    private static class DummyTabFactory implements TabHost.TabContentFactory {
      private final Context mContext;

      public DummyTabFactory(Context context) {
        mContext = context;
      }

      @Override
      public View createTabContent(String tag) {
        View v = new View(mContext);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        return v;
      }
    }

    public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
      mActivity = activity;
      mTabHost = tabHost;
      mContainerId = containerId;
      mTabHost.setOnTabChangedListener(this);
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
      tabSpec.setContent(new DummyTabFactory(mActivity));
      String tag = tabSpec.getTag();

      TabInfo info = new TabInfo(tag, clss, args);

      // Check to see if we already have a fragment for this tab, probably
      // from a previously saved state.  If so, deactivate it, because our
      // initial state is that a tab isn't shown.
      info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
      if (info.fragment != null && !info.fragment.isDetached()) {
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        ft.detach(info.fragment);
        ft.commit();
      }

      mTabs.put(tag, info);
      mTabHost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String tabId) {
      TabInfo newTab = mTabs.get(tabId);
      if (mLastTab != newTab) {
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        if (mLastTab != null) {
          if (mLastTab.fragment != null) {
            ft.detach(mLastTab.fragment);
          }
        }
        if (newTab != null) {
          if (newTab.fragment == null) {
            newTab.fragment = Fragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
            ft.add(mContainerId, newTab.fragment, newTab.tag);
          } else {
            ft.attach(newTab.fragment);
          }
        }

        mLastTab = newTab;
        ft.commit();
        mActivity.getSupportFragmentManager().executePendingTransactions();
      }
    }
  }
}
