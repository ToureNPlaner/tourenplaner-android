package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.UserInput;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NodelistScreen extends Activity {
	private ListView ListviewNode;
	private String listviewArray[];
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.nodelistscreen);
	//TODO should be dynamic
//	listviewArray=new String[3];
//	listviewArray[0]="Node1";
//	listviewArray[1]="Node2";
//	listviewArray[2]="Node3";
	listviewArray = new String[NodeModel.getNodeVector().size()];
	for (int i = 0; i < NodeModel.getNodeVector().size();i++){
		listviewArray[i] = NodeModel.getNodeVector().get(i).getName();
	}
	
	ListviewNode=(ListView)findViewById(R.id.listviewNode);
	// By using setAdpater method in listview we an add string array in list.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1 , listviewArray);
	ListviewNode.setAdapter(adapter);
	
	ListviewNode.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int pos,
				long arg3) {
			UserInput.setSelectedNode(pos);
			// TODO logic of Nodelist click
			// generates an intent from the class NodeListScreen
			Intent myIntent = new Intent(NodelistScreen.this, NodeScreen.class);
			startActivity(myIntent);
						
		}
	});
	}

}
