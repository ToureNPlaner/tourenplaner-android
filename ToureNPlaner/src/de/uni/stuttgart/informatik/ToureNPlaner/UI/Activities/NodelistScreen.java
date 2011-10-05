package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import java.util.Vector;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.SessionData;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Adapters.NodeListAdapter;

public class NodelistScreen extends ListActivity {
	public NodelistScreen nodeListScreenContext = this;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        SessionData.Instance.save(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SessionData.Instance.load(savedInstanceState);
    }
	
	private ListView listView;
	private static NodeListAdapter adapter;
		public static  NodeListAdapter getAdapter() {
		return adapter;
	}
	public void setAdapter(NodeListAdapter adapter) {
		NodelistScreen.adapter = adapter;
	}
	
	
		@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Vector<Node> nodeList = NodeModel.getInstance().getNodeVector();
		adapter = new NodeListAdapter(nodeList, this);
		listView = getListView();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view,
					final int pos, long arg3) {
				SessionData.Instance.setSelectedNode(pos);
				// generates an intent from the class NodeListScreen
				Intent myIntent = new Intent(nodeListScreenContext,
						NodePreferences.class);
				startActivity(myIntent);
				getAdapter().notifyDataSetChanged();
				}
	});
	}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
		        Log.v("back button pressed","back button pressed");
		        MapScreen.printAllMarkersToMap();
				}
		    return super.onKeyDown(keyCode, event);
		}
		
		public static void getNodePreferences(Integer pos){
		
		}
};
