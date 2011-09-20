package de.uni.stuttgart.informatik.ToureNPlaner.Data;


	import java.util.Vector;
	import android.content.Context;
	import android.view.LayoutInflater;
	import android.view.View;
	import android.view.ViewGroup;
	import android.widget.BaseAdapter;
	import android.widget.LinearLayout;
	import android.widget.TextView;
	import de.uni.stuttgart.informatik.ToureNPlaner.R;

	public class NodeAdapter extends BaseAdapter{
		  private  Vector<Node> nodeVector;
		  private Context context;
		  String[] propertiesVector;
		public NodeAdapter(Vector<Node> nodeVector,String[] propertiesVector, Context context) {
			 this.propertiesVector = propertiesVector;
			 this.nodeVector = nodeVector;
			 this.context = context;
		}
		
		 @Override
	     public View getView(int position, View convertView, ViewGroup parent) {
			  LinearLayout itemLayout;
		        Node node = nodeVector.get(position);
		 
		        itemLayout= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.nodeadapteritem, parent, false);
		 
		        TextView tvUser = (TextView) itemLayout.findViewById(R.id.toptext);
		        tvUser.setText(propertiesVector[0]);
		 
		        TextView tvText = (TextView) itemLayout.findViewById(R.id.bottomtext);
		        tvText.setText(node.getName());
//		        switch (position){
//		        case 0 :  tvText.setText(node.getName());  break;
//		        case 1 : tvText.setText(node.getGeoPoint().toString()); break;
//		        case 2 : tvText.setText(node.getGeoPoint().toString()); break;
//		        default:  tvText.setText(node.getGeoPoint().toString()); break;
//		            }
	
		 
		        return itemLayout;
	     }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return nodeVector.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return nodeVector.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		

	}
