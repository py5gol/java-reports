package br.com.geodev.app.relatoriosweb.dao;

import br.com.geodev.app.relatoriosweb.bean.Parametro;
import br.com.geodev.app.relatoriosweb.bean.Relatorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelatorioDAO {

    private PreparedStatement stmtInsert;
    private PreparedStatement stmtUpdate;
    private PreparedStatement stmtDelete;
    private PreparedStatement stmtGetJasper;
    private Statement stmt;
    private StringBuffer insertSql;
    private StringBuffer updateSql;
    private StringBuffer deleteSql;
    private StringBuffer getJasperSql;
    private StringBuffer sql;
    private ArrayList<Relatorio> query;
    private Relatorio report;
    private Connection con;

    public RelatorioDAO(Connection conn) throws SQLException {
        con = conn;
        insertSql = new StringBuffer("INSERT INTO tb_relatorio (titulo, nome, arquivo, arquivo_jrxml) VALUES (?,?,?,?) ");
        updateSql = new StringBuffer("UPDATE tb_relatorio SET titulo = ?, arquivo = ?, arquivo_jrxml = ? WHERE id_relatorio = ? ");
        deleteSql = new StringBuffer("DELETE FROM tb_relatorio WHERE id_relatorio = ?");
        getJasperSql = new StringBuffer("SELECT arquivo FROM tb_relatorio WHERE id_relatorio = ?");

        sql = new StringBuffer();
        query = new ArrayList<Relatorio>();
        stmtInsert = conn.prepareStatement(insertSql.toString());
        stmtUpdate = conn.prepareStatement(updateSql.toString());
        stmtDelete = conn.prepareStatement(deleteSql.toString());
        stmtGetJasper = conn.prepareStatement(getJasperSql.toString());
        
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    public Integer save(Relatorio r) throws SQLException {
        Integer retorno = null;
        if (r.getIdRelatorio() == 0) {
            stmtInsert.setString(1, r.getTitulo());
            stmtInsert.setString(2, r.getNome());
            stmtInsert.setBytes(3, r.getArquivo());
            //stmtInsert.setBinaryStream(3, r.getArquivo(), r.getTamanho().intValue());
            stmtInsert.setBytes(4, r.getArquivo_jrxml());
            //stmtInsert.setBinaryStream(4, r.getArquivo_jrxml(), r.getTamanho_jrxml().intValue());
            retorno = stmtInsert.executeUpdate();
        } else {
            stmtUpdate.setString(1, r.getTitulo());
            stmtUpdate.setBytes(2, r.getArquivo());
            //stmtUpdate.setBinaryStream(2, r.getArquivo(), r.getTamanho().intValue());
            stmtUpdate.setBytes(3, r.getArquivo_jrxml());
            //stmtUpdate.setBinaryStream(3, r.getArquivo_jrxml(), r.getTamanho_jrxml().intValue());
            stmtUpdate.setInt(4, r.getIdRelatorio());
            retorno = stmtUpdate.executeUpdate();
        }
        return retorno;
    }

    public Integer delete(Relatorio r) throws SQLException {
        stmtDelete.setInt(1, r.getIdRelatorio());
        return stmtDelete.executeUpdate();
    }

    public ArrayList<Relatorio> list() throws SQLException {
        query.clear();

        sql.delete(0, sql.length());
        sql.append("SELECT id_relatorio, titulo, nome, arquivo, arquivo_jrxml, id_relatorio_mestre ").append("FROM tb_relatorio ").append("WHERE id_relatorio_mestre IS NULL ").append("ORDER BY titulo");

        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql.toString());

        while (rs.next()) {
            report = new Relatorio();
            report.setIdRelatorio(rs.getInt("id_relatorio"));
            report.setTitulo(rs.getString("titulo"));
            report.setNome(rs.getString("nome"));
            report.setArquivo(rs.getBytes("arquivo"));
            //report.setArquivo(rs.getBinaryStream("arquivo"));
            report.setArquivo_jrxml(rs.getBytes("arquivo_jrxml"));
            //report.setArquivo_jrxml(rs.getBinaryStream("arquivo_jrxml"));
            report.setIdRelatorioMestre(rs.getInt("id_relatorio_mestre"));

            loadFields(report);
            loadSubreports(report);

            query.add(report);

        }

        rs.close();
        stm.close();

        return query;
    }

    public Relatorio find(Integer id) throws SQLException {
        sql.delete(0, sql.length());
        sql.append("SELECT id_relatorio, titulo, nome, arquivo, arquivo_jrxml, id_relatorio_mestre ").append("FROM tb_relatorio ").append("WHERE id_relatorio=").append(id);

        ResultSet rs = stmt.executeQuery(sql.toString());

        if (rs.next()) {
            report = new Relatorio();
            report.setIdRelatorio(rs.getInt("id_relatorio"));
            report.setTitulo(rs.getString("titulo"));
            report.setNome(rs.getString("nome"));
            report.setArquivo(rs.getBytes("arquivo"));
            //report.setArquivo(rs.getBinaryStream("arquivo"));
            report.setArquivo_jrxml(rs.getBytes("arquivo_jrxml"));
            //report.setArquivo_jrxml(rs.getBinaryStream("arquivo_jrxml"));
            report.setIdRelatorioMestre(rs.getInt("id_relatorio_mestre"));

            rs.close();

            loadFields(report);
            loadSubreports(report);

            return report;

        } else {
            return null;

        }

    }

    private void loadFields(Relatorio report) throws SQLException {
        sql.delete(0, sql.length());
        sql.append("SELECT * ").append("FROM tb_relatorio_parametro ").append("WHERE id_relatorio=").append(report.getIdRelatorio());

        ResultSet rs = stmt.executeQuery(sql.toString());

        ArrayList<Parametro> params = new ArrayList<Parametro>();

        while (rs.next()) {
            Parametro p = new Parametro();
            p.setIdRelatorio(report.getIdRelatorio());
            p.setIdParametro(rs.getInt("id_parametro"));
            p.setNome(rs.getString("nome"));
            p.setTipo(rs.getString("tipo"));
            p.setTipoHtml(rs.getString("tipo_html"));
            p.setLabelHtml(rs.getString("label_html"));
            p.setClasse(rs.getString("classe"));
            p.setMetodo(rs.getString("metodo"));
            p.setNomeRelatorio(report.getNome());
            p.setPropriedadeKey(rs.getString("propriedade_chave"));
            p.setPropriedadeDescricao(rs.getString("propriedade_descricao"));
            params.add(p);

        }

        //rs.close();

        report.setParametros(params);
    }

    private void loadSubreports(Relatorio report) throws SQLException {
        sql.delete(0, sql.length());
        sql.append("SELECT * ");
        sql.append("FROM tb_relatorio ");
        sql.append("WHERE id_relatorio_mestre=");
        sql.append(report.getIdRelatorio());

        ResultSet rs = stmt.executeQuery(sql.toString());

        ArrayList<Relatorio> subrels = new ArrayList<Relatorio>();

        while (rs.next()) {
            Relatorio r = new Relatorio();
            r.setIdRelatorio(rs.getInt("id_relatorio"));
            r.setIdRelatorioMestre(rs.getInt("id_relatorio_mestre"));
            r.setNome(rs.getString("nome"));
            r.setTitulo(rs.getString("titulo"));
            r.setArquivo(rs.getBytes("arquivo"));
            //r.setArquivo(rs.getBinaryStream("arquivo"));
            r.setArquivo_jrxml(rs.getBytes("arquivo_jrxml"));
            //r.setArquivo_jrxml(rs.getBinaryStream("arquivo_jrxml"));

            subrels.add(r);
        }

        //rs.close();

        report.setSubrelatorios(subrels);
    }

    public byte[] getJasper(int id) {
        try {
            stmtGetJasper.setInt(1, id);
            ResultSet rs = stmtGetJasper.executeQuery();
            if (rs.next()) {
                byte[] jasper = rs.getBytes("arquivo");

                return jasper;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
}
