package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.NodeListAdapter;

import java.io.Serializable;

public class NodelistScreen extends ListActivity {
    private Session session;
    private NodeListAdapter adapter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Session.IDENTIFIER, session);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }

        adapter = new NodeListAdapter(session.getNodeModel().getNodeVector(), this);
        ListView listView = getListView();
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        
        
        
        //---------ContextMenu-----------------
          listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
          @Override
          public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
          	//if (view.getId() == R.id.serverListView) {
                  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
                  contextMenu.setHeaderTitle(adapter.getItem(info.position).getName());
                  String[] menuItems = {"edit", "delete"};
                  for (int i = 0; i < menuItems.length; i++) {
                      contextMenu.add(Menu.NONE, i, i, menuItems[i]);
                //  }
              }
          }
          }
          );
          
          listView.setOnItemClickListener(new OnItemClickListener() {
          	@Override
              public void onItemClick(AdapterView<?> adapter, View view,
                                      final int pos, long arg3) {
             
                  Intent myIntent = new Intent(NodelistScreen.this,
                          NodePreferences.class);
                  myIntent.putExtra("node", (Serializable) adapter.getItemAtPosition(pos));
                  startActivityForResult(myIntent, pos);
        
              }
          });
   }
      
      @Override
      public boolean onContextItemSelected(MenuItem item) {
          final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
          switch (item.getItemId()) {
              case 0: // edit
              	 Intent myIntent = new Intent(NodelistScreen.this,
                           NodePreferences.class);
                   myIntent.putExtra("node", (Serializable) adapter.getItem(info.position));
                   startActivityForResult(myIntent, info.position);
                  break;
              case 1: // delete
                  
                  break;
          }
          return true;
      }
      



     

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                session.getNodeModel().getNodeVector().set(requestCode, (Node) data.getSerializableExtra("node"));
                adapter.notifyDataSetChanged();
                break;
            case NodePreferences.RESULT_DELETE:
                session.getNodeModel().getNodeVector().remove(requestCode);
                adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent data = new Intent();
            data.putExtra(NodeModel.IDENTIFIER, session.getNodeModel());
            setResult(RESULT_OK, data);
        }
        return super.onKeyDown(keyCode, event);
    }
}
