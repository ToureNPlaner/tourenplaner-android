

package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import java.io.Serializable;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import de.uni.stuttgart.informatik.ToureNPlaner.R;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Node;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.NodeModel;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.NodeListAdapter;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.DragDrop.DragNDropListView;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Listener.DragListener;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Listener.DropListener;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Listener.RemoveListener;

public class NodelistScreen extends ListActivity {
	private NodeListAdapter adapter;
	private Session session;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState != null) {
            session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
        } else {
            session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
        }

        setContentView(R.layout.dragndroplistview);
        
       
        adapter = new NodeListAdapter(session.getNodeModel().getNodeVector(), this);
        setListAdapter(adapter);
        ListView listView = getListView();
        registerForContextMenu(listView);
        
        
        
    if (listView instanceof DragNDropListView) {
    	((DragNDropListView) listView).setDropListener(mDropListener);
    	((DragNDropListView) listView).setRemoveListener(mRemoveListener);
    	((DragNDropListView) listView).setDragListener(mDragListener);
    
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
                          EditNodeScreen.class);
                  myIntent.putExtra("node", (Serializable) adapter.getItemAtPosition(pos));
                  startActivityForResult(myIntent, pos);
        
              }
          });
        
      
        }
    }

	private DropListener mDropListener = 
		new DropListener() {
        public void onDrop(int from, int to) {
        	ListAdapter adapter = getListAdapter();
        	if (adapter instanceof NodeListAdapter) {
        		((NodeListAdapter)adapter).onDrop(from, to);
        		getListView().invalidateViews();
        	}
        }
    };
    
    private RemoveListener mRemoveListener =
        new RemoveListener() {
        public void onRemove(int which) {
        	ListAdapter adapter = getListAdapter();
        	if (adapter instanceof NodeListAdapter) {
        		((NodeListAdapter)adapter).onRemove(which);
        		getListView().invalidateViews();
        	}
        }
    };
    
    private DragListener mDragListener =
    	new DragListener() {

    	int backgroundColor = 0xe0103010;
    	int defaultBackgroundColor;
    	
			public void onDrag(int x, int y, ListView listView) {
				// TODO Auto-generated method stub
			}

			public void onStartDrag(View itemView) {
				itemView.setVisibility(View.INVISIBLE);
				defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
				itemView.setBackgroundColor(backgroundColor);
				ImageView iv = (ImageView)itemView.findViewById(R.id.nodelisticon);
				if (iv != null) iv.setVisibility(View.INVISIBLE);
			}

			public void onStopDrag(View itemView) {
				itemView.setVisibility(View.VISIBLE);
				itemView.setBackgroundColor(defaultBackgroundColor);
				ImageView iv = (ImageView)itemView.findViewById(R.id.nodelisticon);
				if (iv != null) iv.setVisibility(View.VISIBLE);
			}
    	
    };
    
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0: // edit
            	 Intent myIntent = new Intent(NodelistScreen.this,
                         EditNodeScreen.class);
                 myIntent.putExtra("node", (Serializable) adapter.getItem(info.position));
                 startActivityForResult(myIntent, info.position);
                break;
            case 1: // delete
            	session.getNodeModel().remove(info.position);
            	adapter.notifyDataSetChanged();
                
                break;
        }
        return true;
    }
    
 
    
    // ----------------Menu-----------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nodelistmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.revertNodes:
        	session.getNodeModel().reverseNodes();
        	adapter.notifyDataSetChanged();
        	
        
            default:
                return super.onOptionsItemSelected(item);
        }
    }



   

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      switch (resultCode) {
          case RESULT_OK:
              session.getNodeModel().getNodeVector().set(requestCode, (Node) data.getSerializableExtra("node"));
              adapter.notifyDataSetChanged();
              break;
          case EditNodeScreen.RESULT_DELETE:
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