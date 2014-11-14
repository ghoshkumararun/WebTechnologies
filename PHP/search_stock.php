<!DOCTYPE html>
<html>
	<head>
		<META http-equiv="Content-type" content="text/html;charset=UTF-8">
		<script>			
			function verify(nod)
			{
				return !(/[^\t\n\r ]/.test(nod));
			}
			function validate(a)
			{
				var x = a.url.value;
				if (x == null || x == "")
				{
					alert("Please enter a company symbol");
					return;
				}
				if(verify(x))
				{
					alert("Provide a proper XML file, not just spaces!!!");
             		return false;
				}	  
			}
			function checking()
			{
				
			}
			
			function check()
			{
				text=document.getElementById("stk").value;
 text.trim();
 if(text=="")
   {
    alert("Please enter a company");
	document.getElementById("stk").focus();
	return false;
   }
  if(isSP(text))
    {
	 alert("Company Symbol contains Space only: Not Valid"); 
	 document.getElementById("stk").focus();
	 return false;
	}
}
	
		</script>
		<style>
			div {font-size:130%;font-weight:bolder;line-height:100%;};
			h2 {text-decoration:underline;border-bottom: 2px;};
			hr {size:1px;color: #000000;}
		</style>
		<title> 
			Market Stock Search
		</title>
	</head>	
	<body>
		<center>
		<h2>Market Stock Search</h2>
		<fieldset style = "border:2pt solid black;width:450px;">
		<form name="FormValidate" action="" method="post">
				Company Symbol
				<input type="text" name="url" style="width: 200px;"/>
				<input type="submit" name= "submit" value="Search" onClick="validate(this.form)"/>
				<br>
				Example: <i>GOOG, MSFT, YHOO, FB, AAPL, ..etc</i>
		</form>
		</fieldset>
		<br>
		<?php 
		if(isset($_POST['submit']) && ($_POST['url']) != "" && str_replace(" ","",$_POST['url']) != "")
		{
			    $txt = str_replace(" ","",$_POST['url']); 
				$val = "";
				$val1 = "";
				$val1 = $val. "http://query.yahooapis.com/v1/public/yql?q=Select%20Name%2C%20Symbol%2C%20LastTradePriceOnly%2C%20Change%2C%20ChangeinPercent%2C%20PreviousClose%2C%20DaysLow%2C%20DaysHigh%2C%20Open%2C%20YearLow%2C%20YearHigh%2C%20Bid%2C%20Ask%2C%20AverageDailyVolume%2C%20OneyrTargetPrice%2C%20MarketCapitalization%2C%20Volume%2C%20Open%2C%20YearLow%20from%20yahoo.finance.quotes%20where%20symbol%3D%22".$txt."%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"; 
				$load = simplexml_load_file($val1);
			if(($load->results->quote->Change) != "")
			{	
			$plus = "+";
			$minus = "-";
			$zero = "0";
			$percent1 = $load->results->quote->ChangeinPercent;
			$lenn = strlen($percent1);
			$percent = substr($percent1,1,$lenn);
			$v1 = substr($load->results->quote->Change,0,1);
			$v2 = strcmp($v1,$plus);
			$v22 = strcmp($v1,$minus);
			$v23 = strcmp($v1,$zero);
			$v3 = "0";
			if ($v2 == $v3)
			{
				$value = "". $load->results->quote->Change;
				$len = strlen ($value);
				$v4 = substr($value,1,$len);
			?>
			<center><h2>Search Results</h2></center>
			<table>
				<tr>
					<td COLSPAN=4 style="border-bottom:2pt solid black;">
						<div style = "float:left; "><?php echo $load->results->quote->Name."(".$load->results->quote->Symbol.")"; ?></div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<?php echo number_format((double)($load->results->quote->LastTradePriceOnly),2)." ";?><img src="http://www-scf.usc.edu/~csci571/2014Spring/hw6/up_g.gif" /><font style="color:green;"><?php echo number_format((double)$v4,2)."(".$percent.")"; ?></font>
					</td>	
				</tr>
			<?php 
			}
			if($v22 == $v3)
			{
				$value1 = "". $load->results->quote->Change;
				$len1 = strlen ($value1);
				$v44 = substr($value1,1,$len1);
			?>
			<center><h2>Search Results</h2></center>	
			<table>
				<tr>
					<td COLSPAN=4 style= "border-bottom:2pt solid black;">
						<div style = "float:left; "><?php echo $load->results->quote->Name."(".$load->results->quote->Symbol.")"; ?></div><?php echo number_format((double)($load->results->quote->LastTradePriceOnly),2)." ";?><img src="http://www-scf.usc.edu/~csci571/2014Spring/hw6/down_r.gif" /><font style="color:red;"><?php echo number_format((double)$v44,2)."(".$percent.")"; ?></font>
					</td>
				</tr>
			<?php 
			}
			if($v23 == $v3)
			{
				$value2 = "". $load->results->quote->Change;
				$len2 = strlen ($value2);
				$v444 = substr($value2,1,$len2);
			?>
			<center><h2>Search Results</h2></center>	
			<table>
				<tr>
					<td COLSPAN=4 style= "border-bottom:2pt solid black;">
						<div style = "float:left; "><?php echo $load->results->quote->Name."(".$load->results->quote->Symbol.")"; ?></div><?php echo number_format((double)($load->results->quote->LastTradePriceOnly),2)." ";?><font style="color:green;"><?php echo number_format((double)$v444,2)."(".$percent.")"; ?></font>
					</td>
				</tr> 	
			<?php
			  $comma = ",";
			}?>
				<tr>
					<td width="200">Prev Close:</td>
					<td width="80">
					<?php echo number_format((double)($load->results->quote->PreviousClose),2) ?></td>
					<td width="210">Days Range:</td>
					<td align="right" width="200"><?php echo number_format((double)($load->results->quote->DaysLow),2) ." - ". number_format((double)($load->results->quote->DaysHigh),2) ?>
					</td>
				</tr>
				<tr>
					<td>Open:</td>
					<td><?php echo number_format((double)($load->results->quote->Open),2) ?></td>
					<td>52wk Range:</td>
					<td align="right"><?php echo number_format((double)($load->results->quote->YearLow),2) ." - ". (number_format((double)($load->results->quote->YearHigh),2)) ?></td>
				</tr>
				<tr>
					<td>Bid:</td>
					<td><?php echo number_format((double)($load->results->quote->Bid),2) ?></td>
					<td>Volume:</td>
					<td align="right"><?php echo number_format((double)($load->results->quote->Volume)) ?></td>
				</tr>
				<tr>
					<td>Ask:</td>
					<td><?php echo number_format((double)($load->results->quote->Ask),2) ?></td>
					<td>Avg Vol:</td>
					<td align="right"><?php echo number_format((double)($load->results->quote->AverageDailyVolume)) ?></td>
				</tr>
				<tr>
					<td>1y Target Est:</td>
					<td><?php echo number_format((double)($load->results->quote->OneyrTargetPrice),2) ?></td>
					<td>Market Cap:</td>
				<?php
					$storeVal = ($load->results->quote->MarketCapitalization);
					$length = strlen($storeVal);
					$value = substr($storeVal,0,$length-1);
					$substring = substr($storeVal,($length-1),$length);
					$valstr = number_format((double)$value,1)."".$substring;
				?>	
					<td align="right"><?php echo $valstr ?></td>
				</tr>	
			</table>
			</center>
			<?php 
					$qry = "http://feeds.finance.yahoo.com/rss/2.0/headline?s=".$txt."&region=US&lang=en-US";
					$load1 = simplexml_load_file($qry);
			if( ($load1->channel->item->title) != "Yahoo! Finance: RSS feed not found" )		
			{?>
			<center>	
					<table>
					<col width="705px">
					<tr>
							<td style="border-bottom:2pt solid black;">
								<div>News Headlines</div>
							</td>
					</tr>
					</table>
			</center>		
					<ul style ="margin-left:320px;">
					<?php
					foreach ($load1->channel->item as $iterate)
					  {
					  ?>
							<li><a href = "<?php echo($iterate->link);?>" target = "_blank" ><?php echo ($iterate->title); ?></a></li>
					<?php  
					  }
			}
			else
			{
				?>
				<center><h2>Financial Company News is not available</h2></center>
				<?php
			}
			}
			else
			{
				if($load->results->quote->Change == "")
				{
					?>
				<center><h2>Stock Information Not Available</h2></center>	
					<?php
					
					return;
				}
			}
		}	
					?> 
</ul>			
		<noscript>
	</body>
</html>
