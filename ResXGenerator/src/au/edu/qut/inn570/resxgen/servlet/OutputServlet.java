package au.edu.qut.inn570.resxgen.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.qut.inn570.resxgen.controller.FileUploadController;

public class OutputServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/resx");
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Content-Disposition","attachment;filename=target."+FileUploadController.staticTargetLanguage+".resx");
	
		PrintWriter out = resp.getWriter();
		// Assuming your json object is **jsonObject**, perform the following, it will return your json object  
		out.print(FileUploadController.uploadedFile);
		out.flush();
	}
}

