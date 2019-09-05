/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sdum.controller.base;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.RequestInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gabriela
 */
public class ExportaTXT extends HttpServlet{

    private static final int BYTES_DOWNLOAD = 1024;
    protected String dirPath;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    
    public void doGet(HttpServletRequest request,
            HttpServletResponse response, String caminho, String arquivo) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition",
                "attachment;filename=downloadname.txt");
        ServletContext ctx = getServletContext();
        InputStream is = ctx.getResourceAsStream("/testing.txt");

        int read = 0;
        byte[] bytes = new byte[BYTES_DOWNLOAD];
        OutputStream os = response.getOutputStream();

        while ((read = is.read(bytes)) != -1) {
            os.write(bytes, 0, read);
        }
        os.flush();
        os.close();
    }
   
}
