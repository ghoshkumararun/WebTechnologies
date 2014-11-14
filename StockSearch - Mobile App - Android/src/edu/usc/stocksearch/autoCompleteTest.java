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
package edu.usc.stocksearch;
import java.io.*;
import java.util.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class autoCompleteTest {	
	
	static String newslinks[][];
	static int len;
	
	boolean newsNotAvailable=false; 
	private static JSONObject jObject = null;
	
	static ArrayList<String> stkList;
	
	public ArrayList<String> autoCom(String sym) throws Exception
	{
		ArrayList<String> tempstkList = new ArrayList<String>();
        String request = "http://autoc.finance.yahoo.com/autoc?query="+sym+"&callback=YAHOO.Finance.SymbolSuggest.ssCallback"; 
        
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
       return tempstkList;
	}
	public HashMap getStockData(String sym) throws Exception
	{
	    HashMap hm = new HashMap();
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
        
        JSONObject news = res.getJSONObject("News");
        JSONArray item = news.getJSONArray("Item");
        
        int n = item.length();
        len=n;
        
        newslinks = new String[n][2];
 
        for (int i = 0; i < n; ++i) 
        {
        	JSONObject nl= item.getJSONObject(i);
        	newslinks[i][0]=nl.getString("Title");
        	newslinks[i][1]=nl.getString("Link");
        	if(newslinks[i][1].equals("No Link"))
        	{
        		newsNotAvailable = true;
        	}
        }
    
      return hm;
        
          
	}
	public static void main(String[] args) throws Exception {
		
		String sym = "AP";
		autoCompleteTest test1 = new autoCompleteTest();
		stkList = test1.autoCom(sym);
		HashMap hm1=test1.getStockData("UHID");
	    System.out.println("---------------------------");
	    
	 // Get a set of the entries
	    Set set = hm1.entrySet();
	      // Get an iterator
	    Iterator ii = set.iterator();
	      // Display elements
	    while(ii.hasNext()) 
	       {
	         Map.Entry me = (Map.Entry)ii.next();
	         System.out.print(me.getKey() + ": ");
	         System.out.println(me.getValue());
	       }
	      
        for (int i = 0; i < len; ++i) 
        {
            System.out.println(newslinks[i][0]);
            System.out.println(newslinks[i][1]);
        }
		
		
		/*System.out.println("---------------------------");
		for (int i = 0; i < stkList.size(); i++)
		    {
			   String item = stkList.get(i);
			   System.out.println(item);
			}

		 */	
	}
}