package com.flerry.tgbot.javadocs;

import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class ParseDocs {
	private static final String REQUEST_EXCEPTION = "Запрос пуст или содержит запрещенные символы!";
	private static String goodLink;
	private static URI url;

	private static void linkParse(String requestWord) throws IOException {
		if (requestWord.length() == 0 || requestWord.contains("\'")) {
			System.out.println(REQUEST_EXCEPTION);

		} else {
			
			try{
			 url = new URIBuilder("https://search.oracle.com/search/search?search_p_main_operator=all&group=Documentation&")		
		    .addParameter("q", requestWord)
		    .addParameter("", "+url%3A%2Fjavase%2F8%2Fdocs%2Fapi&search")
		    .addParameter("Field", requestWord)
		    .addParameter("","&docsets=%2Fjavase%2F8%2Fdocs%2Fapi" )
		    .build();
			}catch(Exception e){
				e.printStackTrace();
			}
			String allLink;
			ArrayList<String> links = new ArrayList<String>();

			Document doc = Jsoup.connect(url.toString()).timeout(20000).get();
			Elements aElements = doc.select("a[href]");
			for (Element link : aElements) {
				allLink = link.attr("abs:href");
				if (!allLink.contains("search")
						&& allLink.contains(requestWord)) {
					links.add(link.attr("abs:href"));

				}

			}

			goodLink = links.get(0);
		}
	}

	public static String docsTextLink(String requestWord) throws IOException {

		ParseDocs.linkParse(requestWord);
		Document doc2 = Jsoup.connect(goodLink).timeout(20000).get();
		String tElements = doc2.select("div[class=block]").first().text();
		return "\nСсылка на документацию:" + " " + goodLink + "\n\n"
				+ "Краткое описание:" + "\n" + tElements;
	}
}
