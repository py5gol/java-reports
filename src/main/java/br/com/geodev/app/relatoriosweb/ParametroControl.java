package br.com.geodev.app.relatoriosweb;

import br.com.geodev.app.relatoriosweb.bean.Parametro;
import br.com.geodev.app.relatoriosweb.dao.ParametroDAO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class ParametroControl extends HttpServlet {
    private static final long serialVersionUID = -2518709536338759823L;

    private static Logger logger;
    private Connection conn;
    private ParametroDAO par;
    @SuppressWarnings("unchecked")
    private ArrayList<Class> classes;

    @SuppressWarnings("unchecked")
    public void init() throws ServletException {
        logger = Logger.getLogger(ParametroControl.class);
        conn = ConnectionFactory.getConnection();
        try {
            par = new ParametroDAO(conn);
            classes = new ArrayList<Class>();
            String packagesDAO = this.getInitParameter("packageDAO");
            if (packagesDAO != null) {
                String packages[] = packagesDAO.split(";");
                for (int i = 0; i < packages.length; i++) {
                    loadClasses(packages[i]);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        catch (ClassNotFoundException e) {
            logger.error(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Parametro p = new Parametro();
            int op = (request.getParameter("op") == null || "".equals(request.getParameter("op")) ? -1 : Integer.parseInt(request.getParameter("op")));
            int idRelatorio = (request.getParameter("id") == null || "".equals(request.getParameter("id")) ? 0 : Integer.parseInt(request
                            .getParameter("id")));
            int idParametro = (request.getParameter("idParametro") == null || "".equals(request.getParameter("idParametro")) ? 0 : Integer
                            .parseInt(request.getParameter("idParametro")));
            if (Constantes.PARAMETRO == op) {
                op = 0;
            }
            carregarCampos(p, request);
            p.setIdRelatorio(idRelatorio);
            p.setIdParametro(idParametro);
            request.setAttribute("id", p.getIdRelatorio());
            switch (op) {
                case Constantes.LOAD_METODOS:
                    String className = request.getParameter("classe");
                    Class loadedClass = Class.forName(className);
                    Method methods[] = loadedClass.getDeclaredMethods();
                    StringBuffer sb = new StringBuffer();
                    sb.append("<Metodos>");
                    for (Method m : methods) {
                        if ((m.getReturnType().equals(List.class) || m.getReturnType().isArray()) && m.getParameterTypes().length == 0) {
                            sb.append("<Metodo>").append(m.getName()).append("</Metodo>");
                        }
                    }
                    sb.append("</Metodos>");
                    response.setContentType("text/xml");
                    response.setHeader("Cache-Control", "no-cache");
                    response.getOutputStream().println(sb.toString());
                    return;
                case Constantes.SALVAR:// Caso salvar
                    par.save(p);
                    break;
                case Constantes.EXCLUIR:// Caso excluir
                    par.delete(p);
                    break;
                case Constantes.SELECIONAR:// Selecionar um relatorio
                    request.setAttribute("parametro", par.find(idParametro));
                case Constantes.CADASTRO:// Sem break mesmo...hehe
                    request.setAttribute(Constantes.CLASSES, classes);
                    this.getServletContext().getRequestDispatcher("/relatorios/parametro.jsp").forward(request, response);
                    break;
            }
            if (op < Constantes.SELECIONAR) {
                ArrayList<Parametro> tmp = par.list(p.getIdRelatorio());
                request.setAttribute("parametros", tmp);
                request.setAttribute("total", tmp.size());
                this.getServletContext().getRequestDispatcher("/relatorios/listaParametro.jsp").forward(request, response);
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarCampos(Parametro p, HttpServletRequest r) throws IllegalAccessException, InvocationTargetException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Enumeration names = r.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            map.put(name, r.getParameterValues(name));
        }
        BeanUtils.populate(p, map);
    }

    @SuppressWarnings("unchecked")
    public void loadClasses(String pckgname) throws ClassNotFoundException {
        // Get a File object for the package
        File directory = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            String path = "/" + pckgname.replace(".", "/");
            URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("sem classes no package " + path);
            }
            directory = new File(resource.getFile());
        }
        catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " (" + directory + ") package invalido");
        }
        if (directory.exists()) {
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".class") && !files[i].contains("$")) {
                    Class cl = Class.forName(pckgname + '.' + files[i].substring(0, files[i].length() - 6));
                    Method methods[] = cl.getDeclaredMethods();
                    boolean ok = false;
                    for (Method m : methods) {
                        if ((m.getReturnType().equals(List.class) || m.getReturnType().isArray()) && m.getParameterTypes().length == 0) {
                            ok = true;
                            break;
                        }
                    }
                    if (ok) {
                        classes.add(cl);
                    }
                }
            }
        }
        else {
            throw new ClassNotFoundException(pckgname + " package invalido");
        }
    }

}