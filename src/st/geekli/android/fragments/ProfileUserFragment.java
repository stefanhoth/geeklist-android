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
import st.geekli.api.type.Company;
import st.geekli.api.type.Stats;
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
  private Resources         resources;
  private User              user;
  private ImageView         imageView;
  private Activity          activity;
  private TextView          screenNameView, userNameView, locationView, companyView, contributionsView,
      mentionsView, pingsView, cardsView, highfivesView, bioView;
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
          cache = loader.loadImage(user.getAvatar().getLarge(), new ImageLoadedListener() {
            @Override
            public void imageLoaded(final Bitmap imageBitmap) {
              activity.runOnUiThread(new Runnable() {
                public void run() {
                  imageView.setImageBitmap(imageBitmap);
                }
              });
            }
          });
          activity.runOnUiThread(new Runnable() {
            public void run() {
              if (cache != null) {
                imageView.setImageBitmap(cache);
              } else {
                imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.geeklist_logo));
              }
            }
          });
        } catch (MalformedURLException e) {
          e.printStackTrace();
        }
        activity.runOnUiThread(new Runnable() {
          public void run() {
            Stats stats = user.getStats();
            Company company = user.getCompany();
            userNameView.setText(user.getName());
            screenNameView.setText(user.getScreenName());
            locationView.setText(user.getLocation());
            if (company != null) {
              companyView.setText(company.getTitle() + " at " + company.getName());
            }
            if (user.getBio() != null) {
              bioView.setText(getString(R.string.profile_bio, user.getBio()));
            }
            if (stats != null) {
              highfivesView.setText(getString(R.string.profile_stats_highfives, stats.getNumberOfHighfives()));
              mentionsView.setText(getString(R.string.profile_stats_mentions, stats.getNumberOfMentions()));
              contributionsView.setText(getString(R.string.profile_stats_contributions,
                                                  stats.getNumberOfContributions()));
              pingsView.setText(getString(R.string.profile_stats_pings, stats.getNumberOfPings()));
              cardsView.setText(getString(R.string.profile_stats_cards, stats.getNumberOfCards()));
            }
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
    View view = inflater.inflate(R.layout.profile, container, false);
    imageView = (ImageView) view.findViewById(R.id.profile_image);
    locationView = (TextView) view.findViewById(R.id.profile_location);
    companyView = (TextView) view.findViewById(R.id.profile_company);
    screenNameView = (TextView) view.findViewById(R.id.profile_screen_name);
    userNameView = (TextView) view.findViewById(R.id.profile_name);
    cardsView = (TextView) view.findViewById(R.id.profile_cards);
    contributionsView = (TextView) view.findViewById(R.id.profile_contributions);
    mentionsView = (TextView) view.findViewById(R.id.profile_mentions);
    highfivesView = (TextView) view.findViewById(R.id.profile_highfives);
    pingsView = (TextView) view.findViewById(R.id.profile_pings);
    return view;
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
