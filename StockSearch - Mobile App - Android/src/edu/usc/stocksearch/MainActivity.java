package edu.usc.stocksearch;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.Menu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	protected static final String TAG1 = "MainActivity";
	private AutoCompleteTextView autocompTV; 
	private Button search_Button, news_Button, fb_Button;
	private ArrayAdapter<String> adapter;
	List<String> country_list = null;
	Context context = null;
	TextView stock_Name, stock_Val, stock_Change, prev_Close, open, bid, ask, yrTarget, dayRange, weekRange, vol, avgVol, MarketCap;
	ImageView indicator, chart;
	
	RelativeLayout resultView, errorView;
	ScrollView main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        context = this;
        Log.d(TAG1, "Starting StockSearch...");

        main = (ScrollView) findViewById(R.id.container);
        ShapeDrawable.ShaderFactory sff = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient lgg = new LinearGradient(0, 0, 0, main.getHeight(),
                    new int[] { 
                	Color.parseColor("#ababab"), 
                	Color.parseColor("#ffffff") }, //substitute the correct colors for these
                    new float[] {
                        0, 1 },
                    Shader.TileMode.REPEAT);
                 return lgg;
            }
        };
        PaintDrawable p = new PaintDrawable();
        p.setShape(new RectShape());
        p.setShaderFactory(sff);
        main.setBackground((Drawable)p);
        resultView = (RelativeLayout) findViewById(R.id.result_view);
        resultView.setVisibility(View.GONE);
        errorView = (RelativeLayout) findViewById(R.id.error_view);
        errorView.setVisibility(View.GONE);
        
        autocompTV = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        search_Button = (Button) findViewById(R.id.searchButton);
        news_Button = (Button) findViewById(R.id.news_button);
        
        // Text Fields
        stock_Name = (TextView) findViewById(R.id.stock_name);
        stock_Val = (TextView) findViewById(R.id.stock_value);
        stock_Change = (TextView) findViewById(R.id.change_value_percentage);
        prev_Close = (TextView) findViewById(R.id.prev_close_val);
        open = (TextView) findViewById(R.id.open_val);
        bid = (TextView) findViewById(R.id.bid_val);
        ask = (TextView) findViewById(R.id.ask_val);
        yrTarget = (TextView) findViewById(R.id.year_target_val);
        dayRange = (TextView) findViewById(R.id.day_range_val);
        weekRange = (TextView) findViewById(R.id.week_range_val);
        vol = (TextView) findViewById(R.id.volume_val);
        avgVol = (TextView) findViewById(R.id.avg_volume_val);
        MarketCap = (TextView) findViewById(R.id.market_cap_val);
        
        indicator = (ImageView) findViewById(R.id.indicator);
        chart = (ImageView) findViewById(R.id.chart);
        
        country_list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
				country_list );
        autocompTV.setAdapter(adapter);
        autocompTV.setThreshold(0);
		autocompTV.addTextChangedListener(new TextWatcher() {         
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        
	        	String a = s.toString();
	        	if(!a.isEmpty()){
	        		new RetrieveStockListTask().execute(a);
		        	
	        	}
	        }

	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) {                

	        }

			@Override
			public void afterTextChanged(Editable s) {
				
			}
	    });
		
		autocompTV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String ci = (String)parent.getItemAtPosition(position);
				autocompTV.setText((ci.split(","))[0]);
				getStockData();
			}
		});
		
		
		search_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG1, "Button CLicked");
				getStockData();
			}
		});
		
    }


    protected void getStockData() {
    	autocompTV.clearFocus();
    	search_Button.requestFocus();
		String symbol = "";
		symbol = autocompTV.getText().toString();
		
		Log.d(TAG1, "getStockData(): Text= " + symbol);
		if(!symbol.isEmpty()){
    		new RetrieveStockDataTask().execute(symbol);
        	
    	}
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    class RetrieveStockListTask extends AsyncTask<String, Void, List<String>> {

        protected List<String> doInBackground(String... urls) {
            try {
            	return Util.getListFromYahoo(urls[0]);
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(List<String> ret) {
        	
        	if (ret != null && !ret.isEmpty()) {
        		Log.d(TAG1, "return server Call with size ="+ret.size());
				country_list.clear();
				adapter.clear();
				country_list = new ArrayList<String>();
				country_list.addAll(ret);
		        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,
						country_list );
		        autocompTV.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
        }
    }
    
    class RetrieveStockDataTask extends AsyncTask<String, Void, HashMap<String, Object>> {

        protected HashMap<String, Object> doInBackground(String... symbol) {
            try {
            	Log.d(TAG1, "RetrieveStockDataTask(): starting server Call...");
            	return Util.getStockData(symbol[0]);
            } catch (Exception e) {
            	e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(HashMap<String, Object> ret) {
        	Log.d(TAG1, "RetrieveStockDataTask(): Returning server Call...");
        	setDatatoUI(ret);
        }
    }
    
    class LoadImageTask extends AsyncTask<HashMap<String, Object>, Void, Void> {

        protected Void doInBackground(HashMap<String, Object>... map) {
            try {
            	Log.d(TAG1, "LoadImageTask(): starting server Call...");
            	setImage(map[0]);
            } catch (Exception e) {
            	e.printStackTrace();
            }
			return null;
        }

        protected void onPostExecute( ) {
        	Log.d(TAG1, "RetrieveStockDataTask(): Returning server Call...");
        }
    }
    HashMap<String, Object> map1;

	public void setDatatoUI(HashMap<String, Object> map) {
		 map1 = map;
		if(null != map && !map.isEmpty()){
			
			Log.d(TAG1, "chnge typr ="+(String)map.get("ChangeType"));
			if(((String)map.get("ChangeType")).equalsIgnoreCase("+")){
				stock_Change.setTextColor(Color.parseColor("#00ff00"));
				indicator.setImageResource(R.drawable.up_g);
			} else if(((String)map.get("ChangeType")).equalsIgnoreCase("-")){
				stock_Change.setTextColor(Color.parseColor("#ff0000"));
				indicator.setImageResource(R.drawable.down_r);
			} else {
				stock_Change.setTextColor(Color.parseColor("#000000"));
				indicator.setImageResource(R.drawable.up_g);
			}
			stock_Name.setText((String)map.get("Name")+"("+(String)map.get("Symbol")+")");
	        stock_Val.setText((String)map.get("LastTradePriceOnly"));
	        stock_Change.setText((String)map.get("Change")+" ("+(String)map.get("ChangeInPercent")+")");
	        prev_Close.setText((String)map.get("PreviousClose"));
	        open.setText((String)map.get("Open"));
	        bid.setText((String)map.get("Bid"));
	        ask.setText((String)map.get("Ask"));
	        yrTarget.setText((String)map.get("OneYearTargetPrice"));
	        dayRange.setText((String)map.get("DaysLow")+" - "+(String)map.get("DaysHigh"));
	        weekRange.setText((String)map.get("YearLow")+" - "+(String)map.get("YearHigh"));
	        vol.setText((String)map.get("Volume"));
	        avgVol.setText((String)map.get("AverageDailyVolume"));
	        MarketCap.setText((String)map.get("MarketCapitalization"));
	        new LoadImageTask().execute(map);
	        if((Boolean)map.get("hasNews")){
	        	news_Button.setEnabled(true);
	        	news_Button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						loadNews(map1);
					}
				});
	        }
	        else
	        	news_Button.setEnabled(false);
	        errorView.setVisibility(View.GONE);
	        resultView.setVisibility(View.VISIBLE);
		} else {
			resultView.setVisibility(View.GONE);
			errorView.setVisibility(View.VISIBLE);
			
		}
	}


	protected void loadNews(HashMap<String, Object> map1) {
		Toast.makeText(getApplicationContext(),
				"Showing "+(Integer)map1.get("newsLength")+" headlines", Toast.LENGTH_LONG).show();
		Intent myIntent = new Intent(MainActivity.this, NewsActivity.class);
		myIntent.putExtra("news", (String[])map1.get("newsh"));
		myIntent.putExtra("links", (String[])map1.get("newslinks"));//Optional parameters
		MainActivity.this.startActivity(myIntent);
	}


	private void setImage(HashMap<String, Object> map) {
		try {
			  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL((String)map.get("StockChartImageURL")).getContent());
			  chart.setImageBitmap(bitmap); 
			} catch (MalformedURLException e) {
			  e.printStackTrace();
			} catch (IOException e) {
			  e.printStackTrace();
			}
	}

}
