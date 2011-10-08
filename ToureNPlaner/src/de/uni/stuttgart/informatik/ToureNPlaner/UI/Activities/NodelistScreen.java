package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Adapters.NodeListAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.SessionData;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import java.util.ArrayList;

public class NodelistScreen extends ListActivity {
    public NodelistScreen nodeListScreenContext = this;

    private static NodeListAdapter adapter;
    private Session session;

    public static NodeListAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }

        ArrayList<Node> nodeList = session.getNodeModel().getNodeVector();
        adapter = new NodeListAdapter(nodeList, this);
        ListView listView = getListView();
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
            Intent data = new Intent();
            data.putExtra(NodeModel.IDENTIFIER,session.getNodeModel());
            setResult(RESULT_OK, data);
        }
        return super.onKeyDown(keyCode, event);
    }
}
