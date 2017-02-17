package br.com.geodev.app.relatoriosweb;

import br.com.geodev.app.relatoriosweb.dao.RelatorioDAO;
import util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelatorioJasper extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Connection conn = ConnectionFactory.getConnection();
            String idS = request.getParameter("id");
            RelatorioDAO dao = new RelatorioDAO(conn);
            byte[] jasper = dao.getJasper(new Integer(idS));
            if (jasper != null) {
                ServletOutputStream servletOutputStream = response.getOutputStream();
                response.setContentType("application/jasper");
                response.setContentLength(jasper.length);
                response.setHeader("Content-Disposition", "attachment;filename=\"" + idS + ".jasper\"");
                servletOutputStream.write(jasper, 0, jasper.length);
                servletOutputStream.flush();
                servletOutputStream.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioJasper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Métodos HttpServlet. Clique no sinal de + à esquerda para editar o código.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
