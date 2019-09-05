package br.com.sdum.controller.base;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.RequestInfo;
import java.io.File;

/**
 * @author Gabriela
 */
public class WebConfig {

    protected String dirPath;
    protected Result result;
    protected RequestInfo request;

    public WebConfig(Result result, RequestInfo request) {

        this.dirPath = request.getRequest().getSession().getServletContext().getRealPath("")
                + "file" + System.getProperty("file.separator");     
        
        this.validaCriaDiretorio(new File(this.dirPath));
    }

    protected final void validaCriaDiretorio(File diretorio) {

        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
    }
}
