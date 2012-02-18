package st.geekli.android.activities;

import st.geekli.android.Api;
import st.geekli.android.R;
import st.geekli.api.GeeklistApiException;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AuthActivity extends Activity {
  private Activity activity;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.auth);
    activity = this;
  }

  @Override
  public void onResume() {
    super.onResume();
    Uri uri = getIntent().getData();
    if (uri != null) {
      Toast.makeText(activity, uri.toString(), Toast.LENGTH_LONG).show();
      String oauth_verifier = uri.getQueryParameter("oauth_verifier");
      try {
        Api.getApi().getAccessToken(null, oauth_verifier);
      } catch (GeeklistApiException e) {
        e.printStackTrace();
      }
    }
  }

  public void onAuthClick(View view) {
    Api.initApi(this);
  }
}
