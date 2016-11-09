package com.flerry.tgbot.javadocs;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseDocs {
	private static String goodlink;

	private static void linkParse(String requestWord) throws IOException {
		String url = "https://search.oracle.com/search/search?search_p_main_operator=all&group=Documentation&q="
				+ requestWord
				+ "+url%3A%2Fjavase%2F8%2Fdocs%2Fapi&searchField="
				+ requestWord + "&docsets=%2Fjavase%2F8%2Fdocs%2Fapi";
		String allLink;
		ArrayList<String> links = new ArrayList<String>();

		Document doc = Jsoup.connect(url).timeout(20000).get();

		Elements aElements = doc.select("a[href]");
		for (Element link : aElements) {
			allLink = link.attr("abs:href");
			if (!allLink.contains("search") && allLink.contains(requestWord)) {
				links.add(link.attr("abs:href"));

			}

		}
		goodlink = links.get(0);

	}

	public static String docsTextLink(String requestWord) throws IOException {

		ParseDocs.linkParse(requestWord);
		Document doc2 = Jsoup.connect(goodlink).timeout(20000).get();
		String tElements = doc2.select("div[class=block]").first().text();
		return "\nСсылка на документацию:" + " " + goodlink + "\n\n"
				+ "Краткое описание:" + "\n" + tElements;
	}
}
