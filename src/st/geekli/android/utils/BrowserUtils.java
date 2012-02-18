package st.geekli.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class BrowserUtils {
  /**
   * 
   * @param activity
   * @param url
   */
  public static void openBrowserWithUrl(Activity activity, String url) {
    Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    activity.startActivity(openBrowserIntent);
  }
}
