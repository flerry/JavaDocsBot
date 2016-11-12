package com.flerry.tgbot.javadocs;

import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

 class ParseDocs {
    private static final String REQUEST_EXCEPTION = "Запрос пуст или содержит запрещенные символы!";
    private static String goodLinkClass;
    private static URI url;

    private static void linkClassParse(String requestWord) throws IOException {
        if (requestWord.length() == 0 || requestWord.contains("\'")) {
            System.out.println(REQUEST_EXCEPTION);

        } else {

            try {
                url = new URIBuilder("https://search.oracle.com/search/search?search_p_main_operator=all&group=Documentation&")
                        .addParameter("q", requestWord)
                        .addParameter("", "+url%3A%2Fjavase%2F8%2Fdocs%2Fapi&search")
                        .addParameter("Field", requestWord)
                        .addParameter("", "&docsets=%2Fjavase%2F8%2Fdocs%2Fapi")
                        .build();
            } catch (Exception e) {
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

            goodLinkClass = links.get(0);

        }
    }

    public static String docsClassTextLink(String requestClass) throws IOException {

        ParseDocs.linkClassParse(requestClass);
        Document doc = Jsoup.connect(goodLinkClass).timeout(20000).get();
        String tElements = doc.select("div[class=block]").first().text();
        if (tElements.length() > 3500) {
            tElements = doc.select("div[class=block]").first().text().substring(1, 3500) + "...";
        }
        return "\nСсылка на документацию:" + " " + goodLinkClass + "\n\n"
                + "Краткое описание:" + "\n" + tElements;
    }

    public static String docsMethodTextLink(String requestClass, String requestMethod) throws IOException {
        String goodLinkMethod;
        ParseDocs.linkClassParse(requestClass);
        Document doc = Jsoup.connect(goodLinkClass).timeout(20000).get();
        Elements aElements = doc.select("a[href]");
        String allLink;
        ArrayList<String> links = new ArrayList<String>();
        for (Element link : aElements) {
            allLink = link.attr("abs:href");
            if (allLink.contains(requestMethod)) {
                links.add(link.attr("abs:href"));


            }
        }
        goodLinkMethod = links.get(0);
        return "\nСсылка на документацию:" + " " + goodLinkMethod;
    }
}