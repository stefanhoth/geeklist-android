package st.geekli.android;

import st.geekli.android.utils.BrowserUtils;
import st.geekli.api.GeeklistApi;
import st.geekli.api.GeeklistApiException;
import android.app.Activity;

public class Api {
  private static GeeklistApi api;

  public static GeeklistApi getApi(Activity activity) {
    if (api == null) {
      initApiWithCreds(activity);
    }
    return api;
  }

  public static void initApi(final Activity activity) {
    new Thread() {
      public void run() {
        api = new GeeklistApi(Configuration.CONSUMER_KEY, Configuration.CONSUMER_SECRET, true);
        try {
          Configuration.OAUTH_REQUEST = api.getRequestToken(Configuration.CALLBACK_URL);
          BrowserUtils.openBrowserWithUrl(activity, Configuration.OAUTH_REQUEST);
        } catch (GeeklistApiException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  public static void initApiWithCreds(final Activity activity) {
    api = new GeeklistApi(Configuration.CONSUMER_KEY,
                          Configuration.CONSUMER_SECRET,
                          Configuration.getAccessToken(activity),
                          Configuration.getAccessTokenSecret(activity),
                          true);
  }
}
