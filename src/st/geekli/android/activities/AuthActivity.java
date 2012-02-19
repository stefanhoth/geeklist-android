package st.geekli.android.activities;

import st.geekli.android.Api;
import st.geekli.android.R;
import st.geekli.api.GeeklistApiException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AuthActivity extends Activity {
  private Activity activity;
  private EditText oobCodeView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.auth);
    oobCodeView = (EditText) findViewById(R.id.auth_oob_code);
    activity = this;
  }

  public void onOkClick(View view) {
    String code = oobCodeView.getText().toString();
    try {
      String foo = Api.getApi().authorize(code);
    } catch (GeeklistApiException e) {
      e.printStackTrace();
    }
  }

  public void onAuthClick(View view) {
    Api.initApi(this);
  }
}
