package com.tibco;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class Writefile
 */
@WebServlet("/Writefile")
public class Writefile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Writefile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
		
		PrintWriter out = response.getWriter();
		
		String fpath ="",permission="";
		Integer replica = 1;	
		
		// configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();

        factory.setRepository(new File("~/"));

        ServletFileUpload upload = new ServletFileUpload(factory);

        
        String fileName1 = "./";
        String fieldname="";
        String fileName = "" ;
        try {
            List<FileItem> formItems = upload.parseRequest(request);
            
           
            if (formItems != null && formItems.size() > 0) {
            	for (FileItem item : formItems) {
            	    // processes only fields that are not form fields
            	    if (!item.isFormField()) {
            	        fileName = new File(item.getName()).getName();
            	        fileName1+=fileName;
//            	        String filePath = File.separator + fileName;
            	        File storeFile = new File(fileName1);
            	        // saves the file on disk
            	        item.write(storeFile);
            	    } else {
            	        
            	        fieldname = item.getFieldName();
            	        
            	        if (fieldname.equals("path")) {
            	        	fpath = item.getString();
            	        } else if (fieldname.equals("permission")) {
            	        	 permission = item.getString();
            	        }else if (fieldname.equals("replication")) {
            	        	replica  = Integer.valueOf(item.getString());
            	        }
            	    }
            	}
            }
            
            
            //out.println("Upload has been done successfully!"+fileName1);
        } catch (Exception ex) {
            out.println(ex.getMessage());
        }


		
		/*
		 * 
		 * http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=CREATE
            [&overwrite=<true |false>][&blocksize=<LONG>][&replication=<SHORT>]
            [&permission=<OCTAL>][&buffersize=<INT>][&noredirect=<true|false>]"
            
            curl -i -X PUT -T config.json "http://34.251.101.50:50075/webhdfs/v1/sample_test/five/config.json?op=CREATE&namenoderpcaddress=ec2-34-242-193-234.eu-west-1.compute.amazonaws.com:8020&createflag=&createparent=true&overwrite=false&permission=750&user.name=hdfs"
            
            curl -i -X PUT -T config.json "http://34.251.101.50:50075/webhdfs/v1/sample_test/five/config.json?op=CREATE&namenoderpcaddress=ec2-34-242-193-234.eu-west-1.compute.amazonaws.com:8020&createflag=&createparent=true&overwrite=false&permission=750&user.name=hdfs"
		 */		
		String nn="ec2-34-242-108-96.eu-west-1.compute.amazonaws.com";
		String rm="34.251.101.50";
        StringBuilder cmd=new StringBuilder("curl -i -X PUT -T config.json \"http://");
		cmd.append(rm).append(":50075/webhdfs/v1").append(fpath+"/"+fileName).append("?op=CREATE&namenoderpcaddress=")
        		.append(nn).append(":8020&createflag=&createparent=true&overwrite=false&").append(permission).append("=750&user.name=hdfs\"&replication=").append(replica+"\"");
        
        Process p=Runtime.getRuntime().exec(cmd.toString()); 
        try {
			p.waitFor();
			 BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
		        String line=reader.readLine(); 
		        while(line!=null) 
		        { 
		           out.println(line); 
		           line=reader.readLine(); 
		        } 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
       
        
        
        
		String path = "http://ec2-34-242-193-234.eu-west-1.compute.amazonaws.com:50070/webhdfs/v1"+fpath+"?op=CREATE&overwrite=true&replication="+replica+"&permission="+permission+"";
		    
		
		/*response.setContentType("application/json");
		int i=0;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
		    for (String line; (line = reader.readLine()) != null;) {
		        br.append(line+"\n");
		    }
		    JsonParser parser = new JsonParser();
		    JsonObject js = parser.parse(br.toString()).getAsJsonObject();

		    out.write("Permission Owner group 		Path \n" );
		    JsonArray array= (JsonArray) js.get("FileStatuses").getAsJsonObject().get("FileStatus");
		    for(;i<array.size();i++) {
		    	JsonObject o= array.get(i).getAsJsonObject();
		    	out.write(o.get("permission") +"   "+o.get("owner")+"   "+o.get("group")+"   "+o.get("pathSuffix")+"\n" );
		    	
		    }
		    out.println(fpath);
            out.println(permission);
		}catch(Exception e) {
			 response.getWriter().write(e.toString());
		}*/
	}

}
