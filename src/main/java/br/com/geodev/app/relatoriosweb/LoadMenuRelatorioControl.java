package br.com.geodev.app.relatoriosweb;

import br.com.geodev.app.relatoriosweb.dao.RelatorioDAO;
import org.apache.log4j.Logger;
import util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoadMenuRelatorioControl extends HttpServlet {
    private static final long serialVersionUID = -2518709536338759823L;

    private static Logger logger;
    private Connection conn;
    private RelatorioDAO rel;

    public void init() throws ServletException {
        logger = Logger.getLogger(LoadMenuRelatorioControl.class);
        conn = ConnectionFactory.getConnection();
        try {
            rel = new RelatorioDAO(conn);
        }
        catch (SQLException e) {
            logger.error(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("consulta", rel.list());
        }
        catch (SQLException e) {
            logger.error(e);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}