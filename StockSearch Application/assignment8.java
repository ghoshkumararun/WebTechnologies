import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import org.json.*;
import java.text.*;
import java.util.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.XML;

public class assignment8 extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		try
		{
			response.setContentType("text/html");
    	   		PrintWriter pw = response.getWriter();

			String company = request.getParameter("cname");

			String urlstr = "http://default-environment-iqpgs3xkjm.elasticbeanstalk.com/assignment8.php?company="+company;
		
			URL url = new URL(urlstr);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setAllowUserInteraction(false);
			InputStream urlStream = url.openStream();
			
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true); 
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(urlStream);

			Element rootNode = doc.getRootElement();
			
			String Name = rootNode.getChildren("Name").get(0).getText();
			String Symbol = rootNode.getChildren("Symbol").get(0).getText();
			
			List Quote= rootNode.getChildren("Quote");
			
			String ChangeType = Quote.getChildText("ChangeType");
			String Change = Quote.getChildText("Change");
			String ChangeInPercent = Quote.getChildText("ChangeInPercent");
			String LastTradePriceOnly = Quote.getChildText("LastTradePriceOnly");
			String PreviousClose = Quote.getChildText("PreviousClose");
			String DaysLow = Quote.getChildText("DaysLow");
			String DaysHigh = Quote.getChildText("DaysHigh");
			String Open = Quote.getChildText("Open");
			String YearLow = Quote.getChildText("YearLow");
			String YearHigh = Quote.getChildText("YearHigh");
			String Bid = Quote.getChildText("Bid");
			String Volume = Quote.getChildText("Volume");
			String Ask = Quote.getChildText("Ask");
			String AverageDailyVolume = Quote.getChildText("AverageDailyVolume");
			String OneYearTargetPrice = Quote.getChildText("OneYearTargetPrice");
			String MarketCapitalization = Quote.getChildText("MarketCapitalization");
			
			//Parse News
			List News= rootNode.getChildren("News");
			List Items =	News.get(0).getChildren("Item");
			
			String [] Title = new String[Items.size()];;
			String [] Link = new String[Items.size()];;
			
			for (int i = 0; i < Items.size(); i++) 
			{ 
				Title[i] = Items.get(i).getChildText("Title");
				Link[i] = Items.get(i).getChildText("Link");
			}
			
			//Parse Stock Chart
			String StockChart = rootNode.getChildren("StockChartImageURL").getText();
			
			//Creating JSON string
			String jsonString = "{\"result\":{";
			jsonString+="\"Name\":\""+Name+"\",";
			jsonString+="\"Symbol\":\""+Symbol+"\",";
			jsonString+="\"Quote\":{";
			jsonString+="\"ChangeType\":\""+ChangeType+"\",";
			jsonString+="\"Change\":\""+Change+"\",";
			jsonString+="\"ChangeInPercent\":\""+ChangeInPercent+"\",";
			jsonString+="\"LastTradePriceOnly\":\""+LastTradePriceOnly+"\",";
			jsonString+="\"PreviousClose\":\""+PreviousClose+"\",";
			jsonString+="\"DaysLow\":\""+DaysLow+"\",";
			jsonString+="\"DaysHigh\":\""+DaysHigh+"\",";
			jsonString+="\"Open\":\""+Open+"\",";
			jsonString+="\"YearLow\":\""+YearLow+"\",";
			jsonString+="\"YearHigh\":\""+YearHigh+"\",";
			jsonString+="\"Bid\":\""+Bid+"\",";
			jsonString+="\"Volume\":\""+Volume+"\",";
			jsonString+="\"Ask\":\""+Ask+"\",";
			jsonString+="\"AverageDailyVolume\":\""+AverageDailyVolume+"\",";
			jsonString+="\"OneYearTargetPrice\":\""+OneYearTargetPrice+"\",";
			jsonString+="\"MarketCapitalization\":\""+MarketCapitalization+"\"";
			jsonString+="},";
			jsonString+="\"News\":{ \"Item\":[";
			
			for(int i=0;i< Items.size();i++)
			{
				jsonString+="{\"Link\":\""+Link[i]+"\",\"Title\":\""+Title[i]+"\"}";
				if(i< (Items.size()-1))
					jsonString+=",";
			}
			
			jsonString+="]},"+"\"StockChartImageURL\":\""+StockChart+"\"}}";
			
			pw.println(jsonString);
			pw.close();
		}
		catch(MalformedURLException e)
		{
			System.out.print(e.getMessage());
		}
		catch(IOException e)
		{
			System.out.print(e.getMessage());
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
   }
}