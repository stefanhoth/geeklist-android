package st.geekli.android.activities;

import st.geekli.android.Api;
import st.geekli.android.Api.ApiListener;
import st.geekli.android.Configuration;
import st.geekli.android.R;
import st.geekli.api.GeeklistApiException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends Activity {
  private Activity      activity;
  private CookieManager cookieManager;
  private WebView       web;

  public static void startActivityForResult(final Activity start) {
    Intent intent = new Intent(start, LoginActivity.class);
    start.startActivityForResult(intent, R.id.requestcode_login);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    web = new WebView(activity);
    setContentView(web);
    CookieSyncManager.createInstance(this);
    cookieManager = CookieManager.getInstance();
    web.getSettings().setJavaScriptEnabled(true);
    web.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        System.out.println("url: " + url);
        if (url.startsWith(Configuration.CALLBACK_URL)) {
          Uri uri = Uri.parse(url);
          final String verifier = uri.getQueryParameter("oauth_verifier");
          if (!verifier.equals("")) {
            new Thread() {
              public void run() {
                try {
                  Api.getApi(activity).retrieveAccessToken(verifier);
                  String accessToken = Api.getApi(activity).getAccessToken();
                  String accessTokenSecret = Api.getApi(activity).getAccessTokenSecret();
                  Configuration.saveAccessData(activity, accessToken, accessTokenSecret);
                  System.out.println("oauth: " + verifier);
                  activity.runOnUiThread(new Runnable() {
                    public void run() {
                      backToPreferencesView(true);
                    }
                  });
                } catch (GeeklistApiException e) {
                  e.printStackTrace();
                  backToPreferencesView(false);
                }
              }
            }.start();
          } else {
            backToPreferencesView(false);
          }
        } else {
          view.loadUrl(url);
        }
        return true;
      }
    });

    Api.initApi(this, new ApiListener() {

      @Override
      public void isInit() {
        activity.runOnUiThread(new Runnable() {
          public void run() {
            web.loadUrl(Configuration.OAUTH_REQUEST);
          }
        });
      }
    });
  }

  @Override
  public void onBackPressed() {
    backToPreferencesView(false);
  }

  void backToPreferencesView(final boolean isSuccess) {
    Intent intent = new Intent();
    setResult(isSuccess ? RESULT_OK : RESULT_CANCELED, intent);
    cookieManager.removeAllCookie();
    finish();
  }
}
