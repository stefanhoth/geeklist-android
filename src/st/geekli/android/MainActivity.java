package st.geekli.android;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	
	private Resources resources; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		resources = getResources();
		
		ListView feed = (ListView) findViewById(R.id.feed);
		List<FeedItem> feedItems = new ArrayList<FeedItem>();
		
		try {
			//String rawFeedData = LoadFile("activities.json", false);
			InputStream is = getResources().openRawResource(R.raw.activites);
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
			JSONArray jsonActivities    = new JSONArray(rawFeedData);
			for (int i = 0; i < jsonActivities.length(); i++) {
				
				JSONObject activity = jsonActivities.getJSONObject(i);
				JSONObject user = activity.getJSONObject("user");
				String activityType = activity.getString("type");
				JSONObject avatar = user.getJSONObject("avatar");
				JSONObject gfk = activity.getJSONObject("gfk");
				FeedItem item = new FeedItem();
				item.user = user.getString("screen_name");
				item.thumbnail = avatar.getString("large");
				if (activityType.equals("micro")) {
					item.content = gfk.getString("status");
				} else if (activityType.equals("card")) {
					item.content = gfk.getString("headline");
				} else if (activityType.equals("repo_contributor")) {
					item.content = "contributes to " + gfk.getString("name");
				} else if (activityType.equals("follow")) {
					item.content = "follows " + gfk.getString("screen_name");
				} else if (activityType.equals("ping")) {
					item.content = "pinged " + gfk.getString("screen_name");
				}
				feedItems.add(item);
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		FeedItemAdapter adapter = new FeedItemAdapter(this, R.layout.feeditem,
				feedItems);
		feed.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	// load file from apps res/raw folder or Assets folder
	public String LoadFile(String fileName, boolean loadFromRawFolder)
			throws IOException {
		// Create a InputStream to read the file into
		InputStream iS; 
		
		if (loadFromRawFolder) {
			// get the resource id from the file name
			int rID = resources.getIdentifier("android.geekli.st:raw/"
					+ fileName, null, null);
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