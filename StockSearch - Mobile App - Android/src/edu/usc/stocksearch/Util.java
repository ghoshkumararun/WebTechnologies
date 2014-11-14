package edu.usc.stocksearch;

/**
 * Yahoo! Web Services Example: search API via POST
 *
 * @author Daniel Jones www.danieljones.org
 * Copyright 2007 Daniel Jones
 *
 * This example illustrates how to perform a web service request via HTTP POST.
 * See the YahooWebServiceGet example if you want to include all named parameters 
 * in the URL as a GET request.
 */

import java.io.*;
import java.util.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Util {	
	 
	private static JSONObject jObject = null;
	
	public static List<String> getListFromYahoo(String sym)
	{
		List<String> tempstkList = new ArrayList<String>();
        String request = "http://autoc.finance.yahoo.com/autoc?query="+sym+"&callback=YAHOO.Finance.SymbolSuggest.ssCallback"; 
        
        try {
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(request);
			
			//method.addParameter("appid","YahooDemo");
      // method.addParameter("query","GO");
			//method.addParameter("results","10");
			
			// Send POST request
			int statusCode = client.executeMethod(method);
			
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			InputStream rstream = null;
			
			// Get the response body
			rstream = method.getResponseBodyAsStream();
			
			// Process the response from Yahoo! Web Services
			BufferedReader br = new BufferedReader(new InputStreamReader(rstream));
			String line="";
			line = br.readLine();
   
			br.close();
			 
			
			int num = line.indexOf("{");
			String final_line=line.substring(num, (line.length()-1));
      
   
			jObject = new JSONObject(final_line);
			
			JSONObject resSet = jObject.getJSONObject("ResultSet");
			
			JSONArray res = resSet.getJSONArray("Result");
			int n = res.length();
			for (int i = 0; i < n; ++i) 
			{
			  String output="";
			  JSONObject stock = res.getJSONObject(i);
			  output+=stock.getString("symbol")+", "+stock.getString("name")+"("+stock.getString("exch")+")";
			  tempstkList.add(output);
			// System.out.println(output);
			 
    
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return tempstkList;
	}
		
	public static HashMap getStockData(String sym) throws Exception
	{
		String newslinks[], newsh[];
		Integer len;
		Boolean hasNews=false; 
		
	    HashMap<String, Object> hm = new HashMap<String, Object>();
        String request = "http://cs-server.usc.edu:21434/examples/servlets/StockSearchServ?stock_name="+sym; 
        
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(request);
        int statusCode = client.executeMethod(method);
        
        if (statusCode != HttpStatus.SC_OK) {
        	System.err.println("Method failed: " + method.getStatusLine());
        }
        InputStream rstream = null;
        
        rstream = method.getResponseBodyAsStream();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(rstream));
        String line="";
        line = br.readLine();
    
        br.close();
        //System.out.println(line);
        jObject = new JSONObject(line);
        
        JSONObject res = jObject.getJSONObject("result");
        
        hm.put("Name", new String(res.getString("Name")));
        hm.put("Symbol", new String(res.getString("Symbol")));
        
        JSONObject quo = res.getJSONObject("Quote");
               
        hm.put("ChangeType", new String(quo.getString("ChangeType")));
        hm.put("Change", new String(quo.getString("Change")));
        hm.put("ChangeInPercent", new String(quo.getString("ChangeInPercent")));
        hm.put("LastTradePriceOnly", new String(quo.getString("LastTradePriceOnly")));
        hm.put("Open", new String(quo.getString("Open")));
        hm.put("YearLow", new String(quo.getString("YearLow")));
        hm.put("YearHigh", new String(quo.getString("YearHigh")));
        hm.put("Volume", new String(quo.getString("Volume")));
        hm.put("OneYearTargetPrice", new String(quo.getString("OneYearTargetPrice")));
        hm.put("Bid", new String(quo.getString("Bid")));
        hm.put("DaysLow", new String(quo.getString("DaysLow")));
        hm.put("DaysHigh", new String(quo.getString("DaysHigh")));
        hm.put("Ask", new String(quo.getString("Ask")));
        hm.put("AverageDailyVolume", new String(quo.getString("AverageDailyVolume")));
        hm.put("PreviousClose", new String(quo.getString("PreviousClose")));
        hm.put("MarketCapitalization", new String(quo.getString("MarketCapitalization")));
        hm.put("StockChartImageURL", new String(res.getString("StockChartImageURL")));
        JSONObject news = res.getJSONObject("News");
        JSONArray item = news.getJSONArray("Item");
        
        int n = item.length();
        len=n;
        
        newslinks = new String[n];
        newsh = new String[n];
 
        for (int i = 0; i < n; ++i) 
        {
        	JSONObject nl= item.getJSONObject(i);
        	newsh[i]=nl.getString("Title");
        	newslinks[i]=nl.getString("Link");
        	if(newslinks[i].equals("No Link"))
        		hasNews = false;
        	else
        		hasNews = true;
        }
        hm.put("hasNews", hasNews);
        hm.put("newsLength",len);
        hm.put("newslinks", newslinks);
        hm.put("newsh", newsh);
        return hm;
        
          
	}
	
}