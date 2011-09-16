package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities.Generic;

import android.app.LauncherActivity.ListItem;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import de.uni.stuttgart.informatik.ToureNPlaner.R;

public class genericList extends ListActivity{
	
private ListView ListviewNodeProperties;
private Object listviewPropertiesArray[];
private ListItem listViewItems;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.nodescreen);
	//TODO should be dynamic
	listviewPropertiesArray=new String[3];
	listviewPropertiesArray[0]="name";
	listviewPropertiesArray[1]="latitude";
	listviewPropertiesArray[2]="longitude";
//	listviewPropertiesArray = new String[3];
//	listviewPropertiesArray[0] = NodeModel.getNodeVector().get(0).getName();
//	listviewPropertiesArray[1] = NodeModel.getNodeVector().get(0).getLatitude();
//	listviewPropertiesArray[2] = NodeModel.getNodeVector().get(0).getLongitude();
	//listviewPropertiesArray[3] = NodeModel.getNodeVector().get(UserInput.getSelectedNode()).getName();
	
	ListviewNodeProperties=(ListView)findViewById(R.id.listNode);
	// By using setAdpater method in listview we an add string array in list.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1 , listviewPropertiesArray);
	ListviewNodeProperties.setAdapter(adapter);

	
	ListviewNodeProperties.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int pos,
				long arg3) {
			// TODO logic of Nodelist click
						
		}
	});

}}
