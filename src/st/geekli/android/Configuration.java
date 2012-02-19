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
  public static final String      CALLBACK_URL                   = "geeklist-android-app://www.test.de/index.php";
  public static String            OAUTH_REQUEST;
  public static String            ACCESS_TOKEN;
  public static String            ACCESS_TOKEN_SECRET;

  public static SharedPreferences prefs;

  public static void initPrefs(Context context) {
    if (prefs == null) {
      prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
  }

  public static boolean isAuth(Context context) {
    initPrefs(context);
    return prefs.getBoolean(PREFERENCE_AUTH, false);
  }

  public static void setAuth(Context context) {
    initPrefs(context);
    prefs.edit().putBoolean(PREFERENCE_AUTH, true).commit();
  }

  public static void saveAccessData(Context context, String accessToken, String accessTokenSecret) {
    initPrefs(context);
    prefs.edit().putString(PREFERENCE_ACCESS_TOKEN, accessToken).commit();
    prefs.edit().putString(PREFERENCE_ACCESS_TOKEN_SECRET, accessTokenSecret).commit();
    setAuth(context);
  }

  public static String getAccessToken(Context context) {
    initPrefs(context);
    if (ACCESS_TOKEN == null) {
      ACCESS_TOKEN = prefs.getString(PREFERENCE_ACCESS_TOKEN, null);
    }
    return ACCESS_TOKEN;
  }

  public static String getAccessTokenSecret(Context context) {
    initPrefs(context);
    if (ACCESS_TOKEN_SECRET == null) {
      ACCESS_TOKEN_SECRET = prefs.getString(PREFERENCE_ACCESS_TOKEN_SECRET, null);
    }
    return ACCESS_TOKEN_SECRET;
  }
}
