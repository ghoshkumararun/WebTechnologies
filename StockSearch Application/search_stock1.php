<?php 
  $text = $_GET["company"];
  session_start();
  $_SESSION['views']=$text;
  $val1 = "http://query.yahooapis.com/v1/public/yql?q=Select%20Name%2C%20Symbol%2C%20LastTradePriceOnly%2C%20Change%2C%20ChangeinPercent%2C%20PreviousClose%2C%20DaysLow%2C%20DaysHigh%2C%20Open%2C%20YearLow%2C%20YearHigh%2C%20Bid%2C%20Ask%2C%20AverageDailyVolume%2C%20OneyrTargetPrice%2C%20MarketCapitalization%2C%20Volume%2C%20Open%2C%20YearLow%20from%20yahoo.finance.quotes%20where%20symbol%3D%22".$text."%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
  $val2 = "http://feeds.finance.yahoo.com/rss/2.0/headline?s=".$text."&region=US&lang=en-US";
  $xml_chart = 'http://chart.finance.yahoo.com/t?s='.$text.'&amp;lang=en-US&amp;amp;width=300&amp;height=180';
  
  $xml_finance = simplexml_load_file($val1);
  $xml_news = simplexml_load_file($val2);
  
  $elemFinance = $xml_finance->results->quote;
  
  $docum = new DOMDocument('1.0');
  $docum->formatOutput = true;
  
  $root_elem = $docum->createElement('result');
  $root_elem = $docum->appendChild($root_elem);
  
  $title_elem = $docum->createElement('Name');
  $title_elem = $root_elem->appendChild($title_elem);
  $text = $docum->createTextNode($elemFinance->Name);
  $text = $title_elem->appendChild($text);
  
  $symbol_elem = $docum->createElement('Symbol');
  $symbol_elem = $root_elem->appendChild($symbol_elem);
  $text = $docum->createTextNode($elemFinance->Symbol);
  $text = $symbol_elem->appendChild($text);
  
  $quote_elem = $docum->createElement('Quote');
  $quote_elem = $root_elem->appendChild($quote_elem);
  
  $change = $elemFinance->Change;
  $change_substr = substr($change,1);
  $sign = substr($change,0,1);
 
  $change_type = $docum->createElement('ChangeType');
  $change_type = $quote_elem->appendChild($change_type);
  $text = $docum->createTextNode($sign);
  $text = $change_type->appendChild($text);
  
  $inner_change = $docum->createElement('Change');
  $inner_change = $quote_elem->appendChild($inner_change);
  $text = $docum->createTextNode($change_substr);
  $text = $inner_change->appendChild($text);
  
  $percent_change = $elemFinance->ChangeInPercent;
  $percent_change_num = substr($percent_change,1);
  $percent_change_symb = substr($percent_change,0,1);
 
  $change_percent = $docum->createElement('ChangeInPercent');
  $change_percent = $quote_elem->appendChild($change_percent);
  $text = $docum->createTextNode($percent_change_num);
  $text = $change_percent->appendChild($text);
 
  $trade_price = $docum->createElement('LastTradePriceOnly');
  $trade_price = $quote_elem->appendChild($trade_price);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->LastTradePriceOnly),2));
  $text = $trade_price->appendChild($text);
  
  $prev_close = $docum->createElement('PreviousClose');
  $prev_close = $quote_elem->appendChild($prev_close);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->PreviousClose),2));
  $text = $prev_close->appendChild($text);
  
  $daysLow = $docum->createElement('DaysLow');
  $daysLow = $quote_elem->appendChild($daysLow);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->DaysLow),2));
  $text = $daysLow->appendChild($text);
  
  $daysHigh = $docum->createElement('DaysHigh');
  $daysHigh = $quote_elem->appendChild($daysHigh);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->DaysHigh),2));
  $text = $daysHigh->appendChild($text);
  
  $open = $docum->createElement('Open');
  $open = $quote_elem->appendChild($open);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->open),2));
  $text = $open->appendChild($text);
  
  $year_low = $docum->createElement('YearLow');
  $year_low = $quote_elem->appendChild($year_low);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->YearLow),2));
  $text = $year_low->appendChild($text);
  
  $year_high = $docum->createElement('YearHigh');
  $year_high = $quote_elem->appendChild($year_high);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->YearHigh),2));
  $text = $year_high->appendChild($text);
  
  $bid = $docum->createElement('Bid');
  $bid = $quote_elem->appendChild($bid);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->Bid),2));
  $text = $bid->appendChild($text);
  
  $volume = $docum->createElement('Volume');
  $volume = $quote_elem->appendChild($volume);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->Volume),2));
  $text = $volume->appendChild($text);
  
  $ask = $docum->createElement('Ask');
  $ask = $quote_elem->appendChild($ask);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->ask),2));
  $text = $ask->appendChild($text);
  
  $avgdailyvolume = $docum->createElement('AverageDailyVolume');
  $avgdailyvolume = $quote_elem->appendChild($avgdailyvolume);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->AverageDailyVolume),2));
  $text = $avgdailyvolume->appendChild($text);
  
  $oneyeartargetprice = $docum->createElement('OneYearTargetPrice');
  $oneyeartargetprice = $quote_elem->appendChild($oneyeartargetprice);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->OneYearTargetPrice),2));
  $text = $oneyeartargetprice->appendChild($text);
  
  $market_capitalisation = $docum->createElement('MarketCapitalization');
  $market_capitalisation = $quote_elem->appendChild($market_capitalisation);
  $text = $docum->createTextNode(number_format(floatval($elemFinance->MarketCapitalization),2));
  $text = $market_capitalisation->appendChild($text);
  
  //News
  $news = $docum->createElement('News');
  $news = $root_elem->appendChild($news);
  
  $elem_News = $xml_news->channel;
  
  foreach($elem_News->children() as $index)
  {
	if($index->getName()=="item")
	{
		$item1 = $docum->createElement('Item');
		$item1 = $news->appendChild($item1);
		
		$title = $docum->createElement('Title');
		$title = $item1->appendChild($title);
		$text = $docum->createTextNode($index->title_elem);
		$text = $title->appendChild($text);
		
		$link = $docum->createElement('Link');
		$link = $item1->appendChild($link);
		$text = $docum->createTextNode($index->link);
		$text = $link->appendChild($text);
	}
  }
  
  //finance chart
  $chart = $docum->createElement('StockChartImageURL');
  $chart = $root_elem->appendChild($chart);
  $text = $docum->createTextNode($xml_chart);
  $text = $chart->appendChild($text);
  
  echo $docum->saveXML();
  
?>  
  
