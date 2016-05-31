package xyz.brnbn.wikisearch.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xyz.brnbn.wikisearch.index.retrieve.Document;
import xyz.brnbn.wikisearch.index.retrieve.IndexRetrieval;

public class LuckyResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LuckyResult() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String q = request.getParameter("query");
		
		if (q==null || q.isEmpty()) {
		
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		else {
			
			ArrayList<Document> rt = IndexRetrieval.retrieve(q);
			String r = "https://en.wikipedia.org/w/index.php?title=" + rt.get(0).getTitle();
			response.sendRedirect(r);
		}
	}
}
