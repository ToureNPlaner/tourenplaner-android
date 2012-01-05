package de.uni.stuttgart.informatik.ToureNPlaner.UI.Activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.UI.Adapters.NodeResultListAdapter;

public class NodeResultlistScreen extends ListActivity {
	private NodeResultListAdapter adapter;
	private Session session;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			session = (Session) savedInstanceState.getSerializable(Session.IDENTIFIER);
		} else {
			session = (Session) getIntent().getSerializableExtra(Session.IDENTIFIER);
		}


		adapter = new NodeResultListAdapter(session.getNodeModel().getNodeVector(), this);
		setListAdapter(adapter);
		ListView listView = getListView();


		//registerForContextMenu(listView);
//        //---------ContextMenu-----------------
//          listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//          @Override
//          public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//          	//if (view.getId() == R.id.serverListView) {
//                  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
//                  contextMenu.setHeaderTitle(adapter.getItem(info.position).getName());
//                  String[] menuItems = {"edit", "delete"};
//                  for (int i = 0; i < menuItems.length; i++) {
//                      contextMenu.add(Menu.NONE, i, i, menuItems[i]);
//                //  }
//              }
//          }
//          }
//          );
//          
//          listView.setOnItemClickListener(new OnItemClickListener() {
//          	@Override
//              public void onItemClick(AdapterView<?> adapter, View view,
//                                      final int pos, long arg3) {
//             
//                  Intent myIntent = new Intent(NodeResultlistScreen.this,
//                          NodePreferences.class);
//                  myIntent.putExtra("node", (Serializable) adapter.getItemAtPosition(pos));
//                  startActivityForResult(myIntent, pos);
//        
//              }
//          });
//        
//      
	}
//    
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        switch (item.getItemId()) {
//            case 0: // edit
//            	 Intent myIntent = new Intent(NodeResultlistScreen.this,
//                         NodePreferences.class);
//                 myIntent.putExtra("node", (Serializable) adapter.getItem(info.position));
//                 startActivityForResult(myIntent, info.position);
//                break;
//            case 1: // delete
//                
//                break;
//        }
//        return true;
//    }
//    
//
//
//
//   
//
//  @Override
//  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//      switch (resultCode) {
//          case RESULT_OK:
//              session.getNodeModel().getNodeVector().set(requestCode, (Node) data.getSerializableExtra("node"));
//              adapter.notifyDataSetChanged();
//              break;
//          case NodePreferences.RESULT_DELETE:
//              session.getNodeModel().getNodeVector().remove(requestCode);
//              adapter.notifyDataSetChanged();
//      }
//  }
//
//  @Override
//  public boolean onKeyDown(int keyCode, KeyEvent event) {
//      if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//          Intent data = new Intent();
//          data.putExtra(NodeModel.IDENTIFIER, session.getNodeModel());
//          setResult(RESULT_OK, data);
//      }
//      return super.onKeyDown(keyCode, event);
//  }
//    
}