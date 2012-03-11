package st.geekli.android.activities;

import st.geekli.android.Api;
import st.geekli.android.Configuration;
import st.geekli.android.R;
import st.geekli.api.GeeklistApiException;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AuthActivity extends Activity {
  private Activity activity;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.auth);
    activity = this;
  }

  public void onResume() {
    super.onResume();
    Uri uri = getIntent().getData();
    if (uri != null) {
      System.out.println(uri.toString());
      final String oauth_verifier = uri.getQueryParameter("oauth_verifier");
      new Thread() {
        public void run() {
          try {
            Api.getApi(activity).retrieveAccessToken(oauth_verifier);
            String accessToken = Api.getApi(activity).getAccessToken();
            String accessTokenSecret = Api.getApi(activity).getAccessTokenSecret();
            Configuration.saveAccessData(activity, accessToken, accessTokenSecret);
            activity.runOnUiThread(new Runnable() {
              public void run() {
                activity.finish();
              }
            });
          } catch (GeeklistApiException e) {
            e.printStackTrace();
          }
        }
      }.start();
    }
  }

  public void onAuthClick(View view) {
    Api.initApi(this);
    view.setEnabled(false);
  }
}
