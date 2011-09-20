package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import java.util.Vector;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.EditPopup;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeListAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.UserInput;

public class NodelistScreen extends ListActivity {
	public NodelistScreen nodeListScreenContext = this;
	private View editPopupLayout;
	private ListView listView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vector<Node> nodeList = NodeModel.getInstance().getNodeVector();
		ListAdapter adapter = new NodeListAdapter(nodeList, this);
		listView = getListView();
		listView.setAdapter(adapter);
		
		
		
		
		
		
				// the context of this activity
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// Inflate the view from a predefined XML layout
				editPopupLayout = inflater.inflate(R.layout.nodepreferences,
						(ViewGroup) findViewById(R.id.popup_element));

				listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view, final int pos,
					long arg3) {
				UserInput.setSelectedNode(pos);
				// generates an intent from the class NodeListScreen
				Intent myIntent = new Intent(nodeListScreenContext, NodePreferences.class);
				startActivity(myIntent);
			//	EditPopup epop = new EditPopup(editPopupLayout,listView);
				//epop.show();
				
				// TODO logic of Nodelist click
				// generates an intent from the class NodeListScreen
				// Intent myIntent = new Intent(NodelistScreen.this,
				// NodeScreen.class);
				// startActivity(myIntent);

				

			}

		});
	}

};
