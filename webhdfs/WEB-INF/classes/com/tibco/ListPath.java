package com.tibco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class ListPath
 */
@WebServlet(urlPatterns = { "/ListPath" }, initParams = {
		@WebInitParam(name = "path", value = "/", description = "defaultPath") })
public class ListPath extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ListPath() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String path = "http://34.242.108.96:50070/webhdfs/v1"+request.getParameter("path")+"?op=LISTSTATUS";
		URL url = new URL(path);
		StringBuilder br = new StringBuilder();
		response.setContentType("application/json");
		int i=0;
		PrintWriter wr = response.getWriter();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
		    for (String line; (line = reader.readLine()) != null;) {
		        br.append(line+"\n");
		    }
		    JsonParser parser = new JsonParser();
		    JsonObject js = parser.parse(br.toString()).getAsJsonObject();

		    wr.write("Permission Owner group 		Path \n" );
		    JsonArray array= (JsonArray) js.get("FileStatuses").getAsJsonObject().get("FileStatus");
		    for(;i<array.size();i++) {
		    	JsonObject o= array.get(i).getAsJsonObject();
		    	wr.write(o.get("permission") +"   "+o.get("owner")+"   "+o.get("group")+"   "+o.get("pathSuffix")+"\n" );
		    	
		    }
		    System.out.println("");
		}catch(Exception e) {
			 response.getWriter().write(e.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
	}

}
