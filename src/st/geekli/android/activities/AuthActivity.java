package st.geekli.android.activities;

import st.geekli.android.Api;
import st.geekli.android.Configuration;
import st.geekli.android.R;
import st.geekli.api.GeeklistApiException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AuthActivity extends Activity {
  private EditText oobCodeView;
  private Activity activity;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.auth);
    oobCodeView = (EditText) findViewById(R.id.auth_oob_code);
    activity = this;
  }

  public void onOkClick(View view) {
    final String code = oobCodeView.getText().toString();
    final String token = Configuration.OAUTH_REQUEST;
    new Thread() {
      public void run() {
        try {
          Api.getApi().getAccessToken(token, code);
        } catch (GeeklistApiException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  public void onAuthClick(View view) {
    Api.initApi(this);
  }
}
