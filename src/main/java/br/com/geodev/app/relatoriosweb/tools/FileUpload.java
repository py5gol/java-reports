package br.com.geodev.app.relatoriosweb.tools;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FileUpload {
    private boolean multipart;
    private ServletRequestContext srv;
    private DiskFileItemFactory factory;
    private ServletFileUpload upload;
    private HashMap<String, FileItem> map;
    @SuppressWarnings("unchecked")
    private List lista;

    @SuppressWarnings("unchecked")
    public FileUpload(HttpServletRequest request) throws FileUploadException, Exception {
        srv = new ServletRequestContext(request);
        multipart = ServletFileUpload.isMultipartContent(srv);
        if (!multipart)
            throw new Exception("erro.nomultipart");
        factory = new DiskFileItemFactory();
        upload = new ServletFileUpload(factory);
        lista = upload.parseRequest(request);
        map = new HashMap<String, FileItem>();
        Iterator i = lista.iterator();
        while (i.hasNext()) {
            FileItem item = (FileItem) i.next();
            map.put(item.getFieldName(), item);
            //System.out.println(item.getFieldName());
        }
    }

    public FileItem getParameter(String key) {
        return map.get(key);
    }

    @SuppressWarnings("unchecked")
    public List getLista() {
        return lista;
    }

    public InputStream getFile(FileItem i) throws IOException {
        return i.getInputStream();
    }
}
