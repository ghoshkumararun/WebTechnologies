import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.* ;
import org.json.* ;
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

public class search_stock extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
				response.setContentType("text/html");
				//request.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String compny = request.getParameter("company");
				String url = "http://default-environment-vjzajmkp38.elasticbeanstalk.com/search_stock.php?company="+compny;
				String input = "";
				URL u = new URL(url);
				URLConnection urlconn = u.openConnection();
				urlconn.setAllowUserInteraction(false);
				InputStream stream = u.openStream();
				SAXBuilder builderr = new SAXBuilder();
				Document doc = null;
				try
				{
					doc = builderr.build(u);
					XMLOutputter xmlOutput = new XMLOutputter();
					String s = xmlOutput.outputString(doc);
					JSONObject jo = XML.toJSONObject(s);
					String val = jo.toString();
					pw.println(val);
				// Parsing Quote
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
	}			
}
