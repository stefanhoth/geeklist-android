package st.geekli.android.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import st.geekli.android.R;
import st.geekli.android.model.TrendingUserItem;
import st.geekli.android.thread.ImageThreadLoader;
import st.geekli.android.thread.ImageThreadLoader.ImageLoadedListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TrendingUserFragment extends Fragment {
  private Resources resources;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    resources = getResources();
  }

  /**
   * The Fragment's UI is just a simple text view showing its instance number.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.trendinguser, container, false);
    ViewPager vp = (ViewPager) v.findViewById(R.id.trendinguser);
    vp.setAdapter(new TrendingUsersAdapter(getActivity(), R.layout.trendinguseritem));
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

  public class TrendingUsersAdapter extends PagerAdapter {
    private final String           TAG         = TrendingUsersAdapter.class.getSimpleName();
    private ImageThreadLoader      imageLoader = new ImageThreadLoader();
    private List<TrendingUserItem> trendingUserItems;
    private int                    resourceId  = 0;

    public TrendingUsersAdapter(Context context, int resourceId) {
      this.resourceId = resourceId;

      trendingUserItems = new ArrayList<TrendingUserItem>();
      try {
        // String rawFeedData = LoadFile("activities.json", false);
        InputStream is = resources.openRawResource(R.raw.trendingusers);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
          Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
          int n;
          while ((n = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, n);
          }
        } finally {
          is.close();
        }

        String rawFeedData = writer.toString();
        JSONArray jsonActivities = new JSONArray(rawFeedData);
        for (int i = 0; i < jsonActivities.length(); i++) {
          JSONObject activity = jsonActivities.getJSONObject(i);
          String activityType = activity.getString("type");
          JSONObject user = activity.getJSONObject("user");
          JSONObject avatar = user.getJSONObject("avatar");
          JSONObject gfk = activity.getJSONObject("gfk");

          TrendingUserItem item = new TrendingUserItem();
          item.setUser(user.getString("screen_name"));
          item.setThumbnail(avatar.getString("large"));
          if (activityType.equals("micro")) {
            item.setContent(gfk.getString("status"));
          } else if (activityType.equals("card")) {
            item.setContent(gfk.getString("headline"));
          } else if (activityType.equals("repo_contributor")) {
            item.setContent("contributes to " + gfk.getString("name"));
          } else if (activityType.equals("follow")) {
            item.setContent("follows " + gfk.getString("screen_name"));
          } else if (activityType.equals("ping")) {
            item.setContent("pinged " + gfk.getString("screen_name"));
          }
          trendingUserItems.add(item);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @Override
    public int getCount() {
      return trendingUserItems.size();
    }

    public TrendingUserItem getItem(int position) {
      return trendingUserItems.get(position);
    }

    @Override
    public Object instantiateItem(View collection, int position) {
      View view;
      TextView textUser;
      TextView textContent;
      final ImageView image;

      LayoutInflater inflater = (LayoutInflater) collection.getContext()
                                                           .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(resourceId, null, false);

      try {
        textUser = (TextView) view.findViewById(R.id.user);
        textContent = (TextView) view.findViewById(R.id.content);
        image = (ImageView) view.findViewById(R.id.icon);
      } catch (ClassCastException e) {
        Log.e(TAG, "Your layout must provide an image and a text view with ID's icon and text.", e);
        throw e;
      }

      TrendingUserItem item = getItem(position);
      Bitmap cachedImage = null;
      try {
        cachedImage = imageLoader.loadImage(item.getThumbnail(), new ImageLoadedListener() {
          public void imageLoaded(Bitmap imageBitmap) {
            image.setImageBitmap(imageBitmap);
            notifyDataSetChanged();
          }
        });
      } catch (MalformedURLException e) {
        Log.e(TAG, "Bad remote image URL: " + item.getThumbnail(), e);
      }

      textUser.setText(item.getUser());
      textContent.setText(item.getContent());

      if (cachedImage != null) {
        image.setImageBitmap(cachedImage);
      }
      ((ViewPager) collection).addView(view, 0);

      return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
      ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
      return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
      return null;
    }
  }
}
