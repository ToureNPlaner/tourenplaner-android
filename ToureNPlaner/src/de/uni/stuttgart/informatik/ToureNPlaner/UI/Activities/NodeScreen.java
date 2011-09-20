package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import java.util.Vector;


import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeAdapter;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;

import android.app.ListActivity;
import android.os.Bundle;

import android.widget.ListAdapter;


public class NodeScreen extends ListActivity {

	private String listviewPropertiesArray[];
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		   super.onCreate(savedInstanceState);
		   Vector<Node> nodeList = NodeModel.getInstance().getNodeVector() ;
		   
		 //TODO should be dynamic
			listviewPropertiesArray=new String[3];
			listviewPropertiesArray[0]="name";
			listviewPropertiesArray[1]="latitude";
			listviewPropertiesArray[2]="longitude";
	       ListAdapter adapter = new NodeAdapter(nodeList,listviewPropertiesArray, this);
	        getListView().setAdapter(adapter);
	
	

//	ListviewNodeProperties.setOnItemClickListener(new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> adapter, View view, int pos,
//				long arg3) {
//			// TODO logic of Nodelist click
//						
//		}
//	});
	}

}
