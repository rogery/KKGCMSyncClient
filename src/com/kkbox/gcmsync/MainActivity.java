package com.kkbox.gcmsync;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import org.json.JSONObject;

import com.kkbox.gcmsync.GCMIntentService.KKGCMListener;
import com.kkbox.gcmsync.KKGCMServer.KKConnectListener;

public class MainActivity extends Activity {

	private ListView listView;
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcmsync);
		listView = (ListView) this.findViewById(R.id.listView);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
	    listView.setAdapter(adapter);
	    
	    GCMIntentService.registerGCM(this);
	    GCMIntentService.registerGCMListener(new KKGCMListener(){
			@Override
			public void onUpdate() {
				syncList();
			}
	    });
	    
	    syncList();
    }

	private void syncList() {
		KKGCMServer.connect("act=pull", new KKConnectListener() {
			@Override
			public void onResponse(JSONObject res) {
				try {
					list.clear();
					final int count = res.optInt("count");
					for(int i=1; i<=count; i++){
						list.add("Item "+i);
					}
					adapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.action_add){
			list.add("Item "+ (list.size() + 1));
			adapter.notifyDataSetChanged();
			KKGCMServer.connect("act=push&count=" + list.size(), null);
		}
		else if(item.getItemId() == R.id.action_del){
			if(list.size() > 0){
				list.remove(list.size()-1);
				adapter.notifyDataSetChanged();
				KKGCMServer.connect("act=push&count="+ list.size(), null);
			}
		}
		return true;
	}
    
}
