package st.geekli.android;

import st.geekli.android.utils.BrowserUtils;
import st.geekli.api.GeeklistApi;
import st.geekli.api.GeeklistApiException;
import android.app.Activity;

public class Api {
  private static GeeklistApi api;

  public static GeeklistApi getApi() {
    return api;
  }

  public static void initApi(final Activity activity) {
    new Thread() {
      public void run() {
        api = new GeeklistApi(Configuration.CONSUMER_KEY, Configuration.CONSUMER_SECRET, false);
        try {
          Configuration.OAUTH_REQUEST = api.getRequestToken(Configuration.CALLBACK_URL);
          BrowserUtils.openBrowserWithUrl(activity, Configuration.OAUTH_REQUEST);
        } catch (GeeklistApiException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }
}
