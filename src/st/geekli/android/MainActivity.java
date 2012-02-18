package st.geekli.android;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.app.Activity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu; 
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class MainActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ListView feed = (ListView) findViewById(R.id.feed);
        
        
        List<FeedItem> feedItems = new ArrayList<FeedItem>();
        
        for(int i=0; i < 10; i++) {
        	FeedItem item = new FeedItem();
        	item.user = "Bill Gates";
        	item.content = "lorem ispum dolor";
        	item.thumbnail = "http://profile.ak.fbcdn.net/hprofile-ak-snc4/276582_216311481960_498814368_q.jpg"; 
        	feedItems.add(item); 
        }
        
        FeedItemAdapter adapter = new FeedItemAdapter(this, R.layout.feeditem, feedItems);
        feed.setAdapter(adapter);
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
		return true;
	}
}