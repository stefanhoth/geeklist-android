package st.geekli.android;

import java.net.MalformedURLException;
import java.util.List;

import st.geekli.android.ImageThreadLoader.ImageLoadedListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedItemAdapter extends ArrayAdapter<FeedItem> {
	  private final static String TAG = "MediaItemAdapter";
	  private int resourceId = 0;
	  private LayoutInflater inflater;
	  private Context context;

	  private ImageThreadLoader imageLoader = new ImageThreadLoader();

	  public FeedItemAdapter(Context context, int resourceId, List<FeedItem> feedItems) {
	    super(context, 0, feedItems);
	    this.resourceId = resourceId;
	    inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    this.context = context;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {

	    View view;
	    TextView textUser;
	    TextView textContent;
	    final ImageView image;

	    view = inflater.inflate(resourceId, parent, false);

	    try {
	      textUser = (TextView)view.findViewById(R.id.user);
	      textContent = (TextView)view.findViewById(R.id.content);
	      image = (ImageView)view.findViewById(R.id.icon);
	    } catch( ClassCastException e ) {
	      Log.e(TAG, "Your layout must provide an image and a text view with ID's icon and text.", e);
	      throw e;
	    }

	    FeedItem item = getItem(position);
	    Bitmap cachedImage = null;
	    try {
	      cachedImage = imageLoader.loadImage(item.thumbnail, new ImageLoadedListener() {
	      public void imageLoaded(Bitmap imageBitmap) {
	      image.setImageBitmap(imageBitmap);
	      notifyDataSetChanged();                }
	      });
	    } catch (MalformedURLException e) {
	      Log.e(TAG, "Bad remote image URL: " + item.thumbnail, e);
	    }

	    textUser.setText(item.user);
	    textContent.setText(item.content);

	    if( cachedImage != null ) {
	      image.setImageBitmap(cachedImage);
	    }

	    return view;
	  }
	}
