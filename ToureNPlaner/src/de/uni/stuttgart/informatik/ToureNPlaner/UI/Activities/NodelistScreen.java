package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.ListActivity;
import android.os.Bundle;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;



public class NodelistScreen extends ListActivity {
	//private ListView ListviewNode;
	//private String listviewArray[];
	 private NodeAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	super.onCreate(savedInstanceState);
	this.setContentView(R.layout.emptylist);
	adapter = new NodeAdapter(this,R.layout.listitem , NodeModel.getInstance().getNodeVector());
	adapter.notifyDataSetChanged();
	this.setListAdapter(adapter);
	
	 
	 //TODO should be dynamic
//	listviewArray=new String[3];
//	listviewArray[0]="Node1";
//	listviewArray[1]="Node2";
//	listviewArray[2]="Node3";
//	listviewArray = new String[NodeModel.getNodeVector().size()];
//	for (int i = 0; i < NodeModel.getNodeVector().size();i++){
//		listviewArray[i] = NodeModel.getNodeVector().get(i).getName();
//	}
	
	//ListviewNode=(ListView)findViewById(R.id.listviewNode);
	// By using setAdpater method in listview we an add string array in list.
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1 , listviewArray);
	
	
	//ListviewNode.setAdapter(adapter);
	
	//ListviewNode.setOnItemClickListener(new OnItemClickListener() {

//		@Override
//		public void onItemClick(AdapterView<?> adapter, View view, int pos,
//				long arg3) {
//			UserInput.setSelectedNode(pos);
//			// TODO logic of Nodelist click
//			// generates an intent from the class NodeListScreen
//			Intent myIntent = new Intent(NodelistScreen.this, NodeScreen.class);
//			startActivity(myIntent);
//						
//		}
//	});
	}

}
