package com.eulerity.hackathon.imagefinder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageScraper{
    private ArrayList<String> imageURLS;

    private String URL;

    public ImageScraper(String url){
        this.URL = url;
        imageURLS = new ArrayList<String>();

        scrapeImageURLS();
    }

    public void scrapeImageURLS(){

        Document document;
        try {
            document = Jsoup.connect(this.URL).ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .timeout(12000)
                    .followRedirects(true)
                    .get();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        Elements elements = document.select("img");

        for (Element element:elements){
            String link = element.attr("abs:src");
            System.out.println(link);
            imageURLS.add(link);
        }
    }
    public ArrayList<String> getURLS() {
        return this.imageURLS;
    }
}
