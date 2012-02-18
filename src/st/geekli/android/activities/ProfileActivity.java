package st.geekli.android.activities;

import st.geekli.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends Activity {
  private Activity activity;
  private TextView nameView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    setContentView(R.layout.profile);
    initUi();
    getProfile();
  }

  private void initUi() {
    nameView = (TextView) findViewById(R.id.profile_name);
  }

  private void getProfile() {
    
  }
}
