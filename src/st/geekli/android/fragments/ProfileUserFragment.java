package st.geekli.android.fragments;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import st.geekli.android.R;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileUserFragment extends Fragment {
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
    View v = inflater.inflate(R.layout.profile, container, false);
//    Bitmap cachedImage = null;
//    ImageThreadLoader imageLoader = new ImageThreadLoader();
//    try {
//      cachedImage = imageLoader.loadImage(item.getThumbnail(), new ImageLoadedListener() {
//        public void imageLoaded(Bitmap imageBitmap) {
//          image.setImageBitmap(imageBitmap);
//        }
//      });
//    } catch (MalformedURLException e) {
//      Log.e(TAG, "Bad remote image URL: " + item.getThumbnail(), e);
//    }
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
