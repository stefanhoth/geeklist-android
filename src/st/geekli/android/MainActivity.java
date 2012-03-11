package st.geekli.android;

import st.geekli.android.activities.LoginActivity;
import st.geekli.android.fragments.ActivityFeedFragment;
import st.geekli.android.fragments.TrendingUserFragment;
import st.geekli.api.GeeklistApiException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
  private Activity  activity;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    activity = this;

    if (!Configuration.isAuth(this)) {
      showLoginDialog();
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
  }

  public void onResume() {
    super.onResume();
    if (Configuration.isAuth(this)) {
      Api.initApiWithCreds(this);
      new Thread() {
        public void run() {
          try {
            System.out.println(Api.getApi(activity).getUser().getName());
          } catch (GeeklistApiException e) {
            e.printStackTrace();
          }
        }
      }.start();
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

  public void showLoginDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.dialog_auth);
    builder.setPositiveButton(R.string.button_accept, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        startActivity(new Intent(activity, LoginActivity.class));
      }
    });
    builder.setNegativeButton(R.string.button_cancel, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        finish();
      }
    });
    builder.setCancelable(false);
    builder.create().show();
  }
}
