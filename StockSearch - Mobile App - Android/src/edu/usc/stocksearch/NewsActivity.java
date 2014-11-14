package edu.usc.stocksearch;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NewsActivity extends ListActivity  {
	
	String newslinks[], newsh[];
	AlertDialog.Builder alertDialog;
	protected ArrayAdapter<String> adapter;
	int pos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.activity_news);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			newsh = extras.getStringArray("news");
			newslinks = extras.getStringArray("links");
		}
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.news_item, newsh));
		 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
				String names[] ={"View","Cancel"};
				pos = position;
				adapter = new ArrayAdapter<String>(NewsActivity.this,android.R.layout.simple_list_item_1,
						names );
				
			    
			    alertDialog = new AlertDialog.Builder(NewsActivity.this);
			    alertDialog.setAdapter(adapter, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(which == 0){
							Toast.makeText(getApplicationContext(),
									"News ="+newsh[pos]+" \nURL= "+newslinks[pos], Toast.LENGTH_LONG).show();
							String url = newslinks[pos];
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse(url));
							startActivity(i);
						}
					}
				});
		        alertDialog.setTitle("View News");
		        
		        alertDialog.show();
			}
		});

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}

	
}
