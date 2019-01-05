package fr.univnantes;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;


@SuppressWarnings("serial")

public class TinyTwitt_Servlet extends HttpServlet{

	static {
		ObjectifyService.register(Twitt.class);
		ObjectifyService.register(User.class);
	}

	
	@Override
	  public void doGet(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {

	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");

	    response.getWriter().print("Bienvenue sur Tiny Twitt , votre réseau favori! 2.0\r\n");

	  }
}
