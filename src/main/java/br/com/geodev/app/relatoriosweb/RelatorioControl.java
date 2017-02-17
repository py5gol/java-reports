package br.com.geodev.app.relatoriosweb;

import br.com.geodev.app.relatoriosweb.bean.Relatorio;
import br.com.geodev.app.relatoriosweb.dao.RelatorioDAO;
import br.com.geodev.app.relatoriosweb.tools.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class RelatorioControl extends HttpServlet {
    private static final long serialVersionUID = -2518709536338759823L;

    private static Logger logger;
    private Connection conn;
    private RelatorioDAO rel;

    public void init() throws ServletException {
        logger = Logger.getLogger(RelatorioControl.class);
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
            boolean multipart = (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data"));
            int id = -1;
            int op = -1;
            Relatorio r = new Relatorio();
            if (multipart) {
                FileUpload f = new FileUpload(request);
                op = (f.getParameter("op") == null ? -1 : Integer.parseInt(f.getParameter("op").getString()));
                id = (f.getParameter("id") == null || "".equals(f.getParameter("id").getString()) ? 0 : Integer.parseInt(f.getParameter("id")
                                .getString()));
                carregarCampos(r, f);
            }
            else {
                op = (request.getParameter("op") == null || "".equals(request.getParameter("op")) ? -1 : Integer.parseInt(request.getParameter("op")));
                id = (request.getParameter("id") == null || "".equals(request.getParameter("id")) ? 0 : Integer.parseInt(request.getParameter("id")));
            }
            r.setIdRelatorio(id);
            switch (op) {
                case Constantes.SUBRELATORIO:
                    this.getServletContext().getRequestDispatcher("/subrelatorio.rel").forward(request, response);
                    return;
                case Constantes.PARAMETRO:
                    this.getServletContext().getRequestDispatcher("/parametro.rel").forward(request, response);
                    return;
                case Constantes.SALVAR:// Caso salvar
                    rel.save(r);
                    break;
                case Constantes.EXCLUIR:// Caso excluir
                    rel.delete(r);
                    break;
                case Constantes.SELECIONAR:// Selecionar um relatorio
                    request.setAttribute("rel", rel.find(id));
                case Constantes.CADASTRO:// Sem break mesmo...hehe
                    this.getServletContext().getRequestDispatcher("/relatorios/relatorioEditar.jsp").forward(request, response);
                    break;
            }
            if (op < Constantes.SELECIONAR) {
                ArrayList<Relatorio> tmp = rel.list();
                request.setAttribute("consulta", tmp);
                request.setAttribute("total", tmp.size());
                this.getServletContext().getRequestDispatcher("/relatorios/lista.jsp").forward(request, response);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
        catch (FileUploadException e) {
            e.printStackTrace();
            logger.error(e);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    private void carregarCampos(Relatorio r, FileUpload f) throws IOException {
        if (f.getParameter("arquivo") != null) {
            byte[] bytes = IOUtils.toByteArray(f.getParameter("arquivo").getInputStream());
            //r.setArquivo(f.getParameter("arquivo").getInputStream());
            r.setArquivo(bytes);
            r.setNome(f.getParameter("arquivo").getName());
            r.setTamanho(f.getParameter("arquivo").getSize());
        }
        if (f.getParameter("arquivo_jrxml") != null) {
            byte[] bytes = IOUtils.toByteArray(f.getParameter("arquivo_jrxml").getInputStream());
            //r.setArquivo_jrxml(f.getParameter("arquivo_jrxml").getInputStream());
            r.setArquivo_jrxml(bytes);
            r.setTamanho_jrxml(f.getParameter("arquivo_jrxml").getSize());
        }
        r.setTitulo(f.getParameter("titulo").getString());
    }
}