package com.eulerity.hackathon.imagefinder;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(
    name = "ImageFinder",
    urlPatterns = {"/main"}
)
public class ImageFinder extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected static final Gson GSON = new GsonBuilder().create();

	//my first attempt at web crawl
	static ImageScraper imageScraper;

	// my attempt at a multithreaded web crawl
	static ImageScraperMulti imageScraperMulti;


	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/json");
		String path = req.getServletPath();
		String url = req.getParameter("url");
		System.out.println("Got request of:" + path + " with query param:" + url);


		try {
			imageScraperMulti = new ImageScraperMulti(url, 3);
			imageScraperMulti.getThread().join();
//			imageScraper = new ImageScraper(url);
//			for (String link: imageScraper.getURLS()) {
//				System.out.println(link);
//			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		resp.getWriter().print(GSON.toJson(imageScraperMulti.getURLS()));
	}
}
