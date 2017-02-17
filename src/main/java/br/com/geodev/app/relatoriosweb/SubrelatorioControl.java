package br.com.geodev.app.relatoriosweb;

import br.com.geodev.app.relatoriosweb.bean.Relatorio;
import br.com.geodev.app.relatoriosweb.dao.SubrelatorioDAO;
import br.com.geodev.app.relatoriosweb.tools.FileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;


public class SubrelatorioControl extends HttpServlet {
    private static final long serialVersionUID = -2518709536338759823L;

    private static Logger logger;
    private Connection conn;
    private SubrelatorioDAO rel;

    public void init() throws ServletException {
        logger = Logger.getLogger(SubrelatorioControl.class);
        conn = ConnectionFactory.getConnection();
        try {
            rel = new SubrelatorioDAO(conn);
        }
        catch (SQLException e) {
            logger.error(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            Enumeration parameters = request.getParameterNames();

            String param = null;
            while (parameters.hasMoreElements()) {
                param = (String) parameters.nextElement();
                System.out.println(param + ": " + request.getParameter(param));
            }

            int id = -1;
            int op = -1;
            int id_mestre = -1;

            boolean multipart = (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data"));
            FileUpload f = null;
            if (multipart) {
                f = new FileUpload(request);
                op = (f.getParameter("op") == null ? -1 : Integer.parseInt(f.getParameter("op").getString()));
                id = (f.getParameter("id") == null || "".equals(f.getParameter("id").getString()) ? 0 : Integer.parseInt(f.getParameter("id").getString()));
                id_mestre = (f.getParameter("id_mestre") == null || "".equals(f.getParameter("id_mestre").getString()) ? 0 : Integer.parseInt(f.getParameter("id_mestre").getString()));
                //carregarCampos(r, f);
            }
            else {
                op = (request.getParameter("op") == null || "".equals(request.getParameter("op")) ? -1 : Integer.parseInt(request.getParameter("op")));
                id = (request.getParameter("id") == null || "".equals(request.getParameter("id")) ? 0 : Integer.parseInt(request.getParameter("id")));
                id_mestre = (request.getParameter("id_mestre") == null || "".equals(request.getParameter("id_mestre")) ? 0 : Integer.parseInt(request.getParameter("id_mestre")));
            }

            request.setAttribute("id_mestre", id_mestre);
            request.setAttribute("id", id);
            request.setAttribute("op", op);

            if (op == Constantes.CADASTRO) {
                this.getServletContext().getRequestDispatcher("/relatorios/subrelatorio.jsp").forward(request, response);

            } else if (op == Constantes.SALVAR) {

                Relatorio r = new Relatorio();

                if (multipart) {
                    carregarCampos(r, f);
                }

                r.setIdRelatorio(0);

                rel.save(r);

            } else if (op == Constantes.EXCLUIR) {
                Relatorio r = new Relatorio();
                r = rel.find(id);
                rel.delete(r);

            }

            ArrayList<Relatorio> tmp = rel.list(new Integer(id_mestre));
            request.setAttribute("consulta", tmp);
            request.setAttribute("total", tmp.size());
            this.getServletContext().getRequestDispatcher("/relatorios/listaSubrelatorio.jsp").forward(request, response);

            /*
            request.setAttribute("rel_mestre", rel.find(id_mestre));
            switch (op) {
                case Constantes.SALVAR:// Caso salvar
                    rel.save(r);
                    break;
                case Constantes.EXCLUIR:// Caso excluir
                    rel.delete(r);
                    break;
                case Constantes.SELECIONAR:// Selecionar um relatorio
                    request.setAttribute("rel", rel.find(id));
                    break;
                case Constantes.CADASTRO:// Sem break mesmo...hehe
                    request.setAttribute("rel", rel.find(id));
                    this.getServletContext().getRequestDispatcher("/relatorios/subrelatorio.jsp").forward(request, response);
                    break;
                case Constantes.SUBRELATORIO: // Listar todos os subrelatorios
            }
            */
        }
        catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
        /*
        catch (FileUploadException e) {
            e.printStackTrace();
            logger.error(e);
        }
        */
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }

    }

    private void carregarCampos(Relatorio r, FileUpload f) throws IOException {
        //InputStream isRelatorio = new ByteArrayInputStream(r.getArquivo());

        if (f.getParameter("arquivo") != null) {
            byte[] bytes = IOUtils.toByteArray(f.getParameter("arquivo").getInputStream());
            r.setArquivo(bytes);
            //r.setArquivo(f.getParameter("arquivo").getInputStream());
            r.setNome(f.getParameter("arquivo").getName());
            r.setTamanho(f.getParameter("arquivo").getSize());
        }

        if (f.getParameter("arquivo_jrxml") != null) {
            byte[] bytes = IOUtils.toByteArray(f.getParameter("arquivo_jrxml").getInputStream());
            //r.setArquivo_jrxml(f.getParameter("arquivo_jrxml").getInputStream());
            r.setArquivo_jrxml(bytes);
            r.setTamanho_jrxml(f.getParameter("arquivo_jrxml").getSize());
            System.out.println(f.getParameter("arquivo_jrxml"));
            System.out.println(f.getParameter("arquivo_jrxml").getInputStream());
            System.out.println(f.getParameter("arquivo_jrxml").getSize());
        }

        r.setIdRelatorioMestre(new Integer(f.getParameter("id_mestre").getString()).intValue());
        r.setTitulo(f.getParameter("titulo").getString());
    }

}
