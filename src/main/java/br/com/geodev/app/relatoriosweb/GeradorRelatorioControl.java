package br.com.geodev.app.relatoriosweb;

import br.com.geodev.app.relatoriosweb.bean.Parametro;
import br.com.geodev.app.relatoriosweb.bean.Relatorio;
import br.com.geodev.app.relatoriosweb.dao.RelatorioDAO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;
import util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class GeradorRelatorioControl extends HttpServlet {
    private static final long serialVersionUID = -2518709536338759823L;

    private static Logger logger;
    private Connection conn;
    private RelatorioDAO rel;

    public void init() throws ServletException {
        logger = Logger.getLogger(GeradorRelatorioControl.class);
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

            int op = (request.getParameter("op") == null || "".equals(request.getParameter("op")) ? 0 : Integer.parseInt(request.getParameter("op")));
            int id = (request.getParameter("id") == null || "".equals(request.getParameter("id")) ? 0 : Integer.parseInt(request.getParameter("id")));
            Relatorio r = rel.find(id);
            switch (op) {
                case Constantes.GERAR_PAGINA:
                    request.setAttribute("html", gerarHtml(r));
                    request.setAttribute("titulo", r.getTitulo());
                    request.setAttribute("id", r.getIdRelatorio());
                    this.getServletContext().getRequestDispatcher("/relatorios/entrada.jsp").forward(request, response);
                    break;
                case Constantes.GERAR_RELATORIO:
                    gerarRelatorio(r, request, response);
                    break;
                case Constantes.GERAR_RELATORIO_FONTE:
                    gerarRelatorioFonte(r, request, response);
                    break;
                default:
                    request.setAttribute("consulta", rel.list());
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
        catch (JRException e) {
            e.printStackTrace();
            logger.error(e);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    @SuppressWarnings("unchecked")
    private String gerarHtml(Relatorio r) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException,
                    NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        StringBuffer html = new StringBuffer();
        for (Parametro p : r.getParametros()) {
            html.append("<tr>\n").append("<td class=\"tabcablat\" width=\"20%\" align=\"right\">").append(p.getLabelHtml()).append(":</td>\n")
                            .append("<td class=\"tabcamlat\" width=\"80%\">");
            if (Constantes.TEXT_FIELD.equals(p.getTipoHtml())) {
                int size = (Constantes.DATE.equals(p.getTipo()) ? 12 : 50);
                String js = "";
                html.append("<input type=\"text\" size=\"").append(size).append("\" name=\"").append(p.getNome()).append("\" maxlength=\"").append(
                                size).append("\" ")
                                .append("/>").append((size == 12 ? js : "")).append("</td>\n").append("</tr>");
                continue;
            }
            if (Constantes.CHECK_FIELD.equals(p.getTipoHtml())) {
                html.append("<input type=\"checkbox\" name=\"").append(p.getNome()).append("\" /></td>\n").append("</tr>");
                continue;
            }
            if (Constantes.LIST_FIELD.equals(p.getTipoHtml())) {
                html.append("<select size=\"1\" name=\"").append(p.getNome()).append("\">");
                Class classe = Class.forName(p.getClasse());
                Object o = classe.newInstance();
                Method metodo = classe.getMethod(p.getMetodo(), new Class[] {});
                String getId = "get";
                String getDescricao = "get";
                String propriedade = p.getPropriedadeKey();

                if (propriedade.charAt(0) >= 97) {
                    char caracter = propriedade.charAt(0);
                    propriedade = propriedade.replace(caracter, (char) (caracter - 32));
                }
                getId += propriedade;
                propriedade = p.getPropriedadeDescricao();

                if (propriedade.charAt(0) >= 97) {
                    char caracter = propriedade.charAt(0);
                    propriedade = propriedade.replace(caracter, (char) (caracter - 32));
                }
                getDescricao += propriedade;

                if (ArrayList.class.equals(metodo.getReturnType())) {
                    ArrayList al = (ArrayList) metodo.invoke(o, new Object[] {});
                    for (int i = 0; i < al.size(); i++) {
                        String id = al.get(i).getClass().getMethod(getId, new Class[] {}).invoke(al.get(i), new Object[] {}).toString();
                        String descricao = al.get(i).getClass().getMethod(getDescricao, new Class[] {}).invoke(al.get(i), new Object[] {}).toString();
                        html.append("<option value=\"").append(id).append("\">").append(descricao).append("</option>\n");
                    }
                }
                else {
                    if (metodo.getReturnType().isArray()) {
                        Object[] arr = (Object[]) metodo.invoke(o, new Object[] {});
                        for (int i = 0; i < arr.length; i++) {
                            String id = arr[i].getClass().getMethod(getId, new Class[] {}).invoke(arr[i], new Object[] {}).toString();
                            String descricao = arr[i].getClass().getMethod(getDescricao, new Class[] {}).invoke(arr[i], new Object[] {}).toString();
                            html.append("<option value=\"").append(id).append("\">").append(descricao).append("</option>\n");
                        }
                    }
                }
                html.append("</select></td>\n").append("</tr>");
                continue;
            }

            if (Constantes.RADIO_FIELD.equals(p.getTipoHtml())) {
                Class classe = Class.forName(p.getClasse());
                Object o = classe.newInstance();
                Method metodo = classe.getMethod(p.getMetodo(), new Class[] {});
                String getId = "get";
                String getDescricao = "get";
                String propriedade = p.getPropriedadeKey();

                if (propriedade.charAt(0) >= 97) {
                    char caracter = propriedade.charAt(0);
                    propriedade = propriedade.replace(caracter, (char) (caracter - 32));
                }
                getId += propriedade;
                propriedade = p.getPropriedadeDescricao();

                if (propriedade.charAt(0) >= 97) {
                    char caracter = propriedade.charAt(0);
                    propriedade = propriedade.replace(caracter, (char) (caracter - 32));
                }
                getDescricao += propriedade;

                if (ArrayList.class.equals(metodo.getReturnType())) {
                    ArrayList al = (ArrayList) metodo.invoke(o, new Object[] {});
                    for (int i = 0; i < al.size(); i++) {
                        String id = al.get(i).getClass().getMethod(getId, new Class[] {}).invoke(al.get(i), new Object[] {}).toString();
                        String descricao = al.get(i).getClass().getMethod(getDescricao, new Class[] {}).invoke(al.get(i), new Object[] {}).toString();
                        html.append("<input value=\"").append(id).append("\" name=\"").append(p.getNome()).append("\" type=\"radio\">").append(
                                        descricao).append("&nbsp;\n");
                    }
                }
                else {
                    if (metodo.getReturnType().isArray()) {
                        Object[] arr = (Object[]) metodo.invoke(o, new Object[] {});
                        for (int i = 0; i < arr.length; i++) {
                            String id = arr[i].getClass().getMethod(getId, new Class[] {}).invoke(arr[i], new Object[] {}).toString();
                            String descricao = arr[i].getClass().getMethod(getDescricao, new Class[] {}).invoke(arr[i], new Object[] {}).toString();
                            html.append("<input value=\"").append(id).append("\" name=\"").append(p.getNome()).append("\" type=\"radio\">").append(
                                            descricao).append("&nbsp;\n");
                        }
                    }
                }
                html.append("</td>\n").append("</tr>");
                continue;
            }
        }
        return html.toString();
    }

    @SuppressWarnings("unchecked")
    private void gerarRelatorio(Relatorio r, HttpServletRequest request, HttpServletResponse response) throws IOException, JRException,
                    ServletException {
        ServletOutputStream servletOutputStream = response.getOutputStream();
        byte[] bytes = null;
        HashMap params = new HashMap();

        InputStream is = new ByteArrayInputStream(r.getArquivo());


        for (Parametro p : r.getParametros()) {
            if (Constantes.STRING.equals(p.getTipo())) {
                params.put(p.getNome(), request.getParameter(p.getNome()));
                continue;
            }
            if (Constantes.INTEGER.equals(p.getTipo())) {
                params.put(p.getNome(), new Integer(request.getParameter(p.getNome())));
                continue;
            }
            if (Constantes.LONG.equals(p.getTipo())) {
                params.put(p.getNome(), new Long(request.getParameter(p.getNome())));
                continue;
            }
            if (Constantes.BOOLEAN.equals(p.getTipo())) {
                params.put(p.getNome(), new Boolean(request.getParameter(p.getNome())));
                continue;
            }
            if (Constantes.DOUBLE.equals(p.getTipo())) {
                params.put(p.getNome(), new Double(request.getParameter(p.getNome())));
                continue;
            }
            if (Constantes.DATE.equals(p.getTipo())) {
                try {
                    params.put(p.getNome(), new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter(p.getNome())));
                }
                catch (ParseException e) {
                    request.setAttribute("erro", "Erro ao converter " + p.getLabelHtml() + "!");
                    this.getServletContext().getRequestDispatcher("/relatorios/entrada.jsp").forward(request, response);
                }
                continue;
            }
        }

        for (Relatorio sub : r.getSubrelatorios()) {
            InputStream isSub = new ByteArrayInputStream(sub.getArquivo());
            JasperReport subReport  = (JasperReport) JRLoader.loadObject(isSub);
            params.put(sub.getTitulo(), subReport);
        }

        if (request.getParameter("extensao_id").equals(Constantes.PDF)) {
            bytes = JasperRunManager.runReportToPdf(is, params, conn);
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + r.getTitulo() + ".pdf\"");
            servletOutputStream.write(bytes, 0, bytes.length);
            servletOutputStream.flush();
            servletOutputStream.close();
        } else {
            if (request.getParameter("extensao_id").equals(Constantes.XLS)){
                ByteArrayOutputStream myBytes = new ByteArrayOutputStream();
                JasperPrint jp = JasperFillManager.fillReport(is, params, conn);
                JRXlsExporter exporter = new JRXlsExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, myBytes);
                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporter.exportReport();

                response.setContentType("application/xls");
                response.setContentLength(myBytes.toByteArray().length);
                response.setHeader("Content-Disposition", "attachment;filename=\"" + r.getTitulo() + ".xls\"");
                servletOutputStream.write(myBytes.toByteArray(), 0, myBytes.toByteArray().length);
                servletOutputStream.flush();
                servletOutputStream.close();
            } else {
                if (request.getParameter("extensao_id").equals(Constantes.RTF)){
                    ByteArrayOutputStream myBytes = new ByteArrayOutputStream();
                    JasperPrint jp = JasperFillManager.fillReport(is, params, conn);
                    JRRtfExporter exporter = new JRRtfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, myBytes);
                    exporter.exportReport();

                    response.setContentType("application/rtf");
                    response.setContentLength(myBytes.toByteArray().length);
                    response.setHeader("Content-Disposition", "attachment;filename=\"" + r.getTitulo() + ".rtf\"");
                    servletOutputStream.write(myBytes.toByteArray(), 0, myBytes.toByteArray().length);
                    servletOutputStream.flush();
                    servletOutputStream.close();
                }
            }
        }
    }

    private void gerarRelatorioFonte(Relatorio r, HttpServletRequest request, HttpServletResponse response) throws IOException, JRException,
                    ServletException {
        ServletOutputStream servletOutputStream = response.getOutputStream();
        HashMap params = new HashMap();

        InputStream is = new ByteArrayInputStream(r.getArquivo());
        byte[] bytes = new byte[r.getTamanho().intValue()];

        int offset = 0;
        int numRead = 0;

        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        response.setContentType("application/jasper");
        response.setContentLength(bytes.length);
        response.setHeader("Content-Disposition", "attachment;filename=\"" + r.getTitulo() + ".jasper\"");
        servletOutputStream.write(bytes, 0, bytes.length);
        servletOutputStream.flush();
        servletOutputStream.close();
    }

}
