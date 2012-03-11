package st.geekli.android.fragments;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import st.geekli.android.Api;
import st.geekli.android.Configuration;
import st.geekli.android.R;
import st.geekli.android.thread.ImageThreadLoader;
import st.geekli.android.thread.ImageThreadLoader.ImageLoadedListener;
import st.geekli.api.GeeklistApiException;
import st.geekli.api.type.User;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileUserFragment extends Fragment {
  private Resources resources;
  private User      user;
  private ImageView imageView;
  private Activity  activity;
  private TextView  screenName, userName, location;
  private ImageThreadLoader loader = new ImageThreadLoader();

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    resources = getResources();
    activity = getActivity();
    if (Configuration.isAuth(getActivity())) {
      Api.initApiWithCreds(getActivity());
      new Thread() {
        public void run() {
          try {
            user = Api.getApi(getActivity()).getUser();
            handleUser(user);
          } catch (GeeklistApiException e) {
            e.printStackTrace();
          }
        }
      }.start();
    }
  }

  private void handleUser(final User user) {
    new Thread() {
      public void run() {
        Looper.prepare();
        final Bitmap cache;
        try {
          System.out.println("a: " + user.getAvatar().getLarge());
          cache = loader.loadImage(user.getAvatar().getLarge(), new ImageLoadedListener() {
            @Override
            public void imageLoaded(final Bitmap imageBitmap) {
              activity.runOnUiThread(new Runnable() {
                public void run() {
                  System.out.println("null: " + imageBitmap != null);
                  imageView.setImageBitmap(imageBitmap);
                }
              });
            }
          });
          if (cache != null) {
            activity.runOnUiThread(new Runnable() {
              public void run() {
                imageView.setImageBitmap(cache);
              }
            });
          }
        } catch (MalformedURLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        activity.runOnUiThread(new Runnable() {
          public void run() {
            userName.setText(user.getName());
            screenName.setText(user.getScreenName());
            location.setText(user.getLocation());
          }
        });
      }
    }.start();
  }

  /**
   * The Fragment's UI is just a simple text view showing its instance number.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.profile, container, false);
    imageView = (ImageView) v.findViewById(R.id.image);
    location = (TextView) v.findViewById(R.id.location);
    screenName = (TextView) v.findViewById(R.id.screen_name);
    userName = (TextView) v.findViewById(R.id.name);
    return v;
  }

  // load file from apps res/raw folder or Assets folder
  public String LoadFile(String fileName, boolean loadFromRawFolder) throws IOException {
    // Create a InputStream to read the file into
    InputStream iS;

    if (loadFromRawFolder) {
      // get the resource id from the file name
      int rID = resources.getIdentifier("android.geekli.st:raw/" + fileName, null, null);
      // get the file as a stream
      iS = resources.openRawResource(rID);
    } else {
      // get the file as a stream
      iS = resources.getAssets().open(fileName);
    }

    // create a buffer that has the same size as the InputStream
    byte[] buffer = new byte[iS.available()];
    // read the text file as a stream, into the buffer
    iS.read(buffer);
    // create a output stream to write the buffer into
    ByteArrayOutputStream oS = new ByteArrayOutputStream();
    // write this buffer to the output stream
    oS.write(buffer);
    // Close the Input and Output streams
    oS.close();
    iS.close();

    // return the output stream as a String
    return oS.toString();
  }
}
