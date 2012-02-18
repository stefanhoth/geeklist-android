package st.geekli.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Configuration {
  public static final String      PREFERENCE_AUTH                = "auth";
  public static final String      PREFERENCE_ACCESS_TOKEN        = "accessToken";
  public static final String      PREFERENCE_ACCESS_TOKEN_SECRET = "accessTokenSecret";
  public static final String      CONSUMER_KEY                   = "LKwGmWaARE9K1M9eNmDgQZlr_VI";
  public static final String      CONSUMER_SECRET                = "fzdra06nX-Gm_2EuFq6ydQzirR-HHITXR6CpF-oNHN8";
  public static final String      CALLBACK_URL                   = "geeklist";
  public static String            OAUTH_REQUEST;
  public static String            ACCESS_TOKEN;
  public static String            ACCESS_TOKEN_SECRET;

  public static SharedPreferences prefs;

  public static boolean isAuth(Context context) {
    if (prefs == null) {
      prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    return prefs.getBoolean(PREFERENCE_AUTH, false);
  }
}
