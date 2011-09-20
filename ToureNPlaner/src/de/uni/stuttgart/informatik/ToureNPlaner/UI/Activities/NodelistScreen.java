package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import java.util.Vector;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeListAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.UserInput;

public class NodelistScreen extends ListActivity {
	// private ListView ListviewNode;
	// private String listviewArray[];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vector<Node> nodeList = NodeModel.getInstance().getNodeVector();
		ListAdapter adapter = new NodeListAdapter(nodeList, this);
		getListView().setAdapter(adapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long arg3) {
				UserInput.setSelectedNode(pos);
				// TODO logic of Nodelist click
				// generates an intent from the class NodeListScreen
				Intent myIntent = new Intent(NodelistScreen.this,
						NodeScreen.class);
				startActivity(myIntent);

			}

		});
	}
}
