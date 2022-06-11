package com.eulerity.hackathon.imagefinder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;

public class ImageScraperMulti implements Runnable{
    private static final int MAX_DEPTH = 3;
    private Thread thread;
    private String first_url;
    private ArrayList<String> visitedLinks;

    private ArrayList<String> imageURLS;
    private int ID;

    public ImageScraperMulti(String url, int num){
        System.out.println("Image Scraper created!");
        this.first_url = url;
        visitedLinks = new ArrayList<String>();
        imageURLS = new ArrayList<String>();
        ID = num;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        imageScrape(1, first_url);
    }

    //recursively looks through the webpage. Stops after a few levels.
    public void imageScrape(int level, String url){
        if(level <= MAX_DEPTH){
            Document document = request(url);

            if (document != null){
                Elements elements = document.select("img");

                for (Element element:elements){
                    String link = element.attr("abs:src");
                    System.out.println(link);
                    imageURLS.add(link);
                }

                //updates the document
                for (Element link: document.select("a[href]")){
                    String next_link = link.absUrl("href");
                    if (visitedLinks.contains(next_link) == false){
                        imageScrape(level ++, next_link);
                    }
                }
            }
        }
    }

    //sets the Document variable given a url
    private Document request(String url){
        try{
            Connection connection = Jsoup.connect(url).ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .timeout(12000)
                    .followRedirects(true);

            Document doc = connection.get();

            //if the request is good, print out the url and add to the visited links list
            if (connection.response().statusCode() == 200){
                System.out.println("\n**Bot ID:" + ID + " received webpage at " + url);

                String title = doc.title();
                System.out.println(title);
                visitedLinks.add(url);
                return doc;
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public Thread getThread(){
        return thread;
    }

    public ArrayList<String> getURLS() {
        return imageURLS;
    }

}
