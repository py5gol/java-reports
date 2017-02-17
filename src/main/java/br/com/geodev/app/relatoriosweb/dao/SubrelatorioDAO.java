package br.com.geodev.app.relatoriosweb.dao;


import br.com.geodev.app.relatoriosweb.bean.Parametro;
import br.com.geodev.app.relatoriosweb.bean.Relatorio;

import java.sql.*;
import java.util.ArrayList;


public class SubrelatorioDAO {
    private PreparedStatement stmtInsert;
    private PreparedStatement stmtUpdate;
    private PreparedStatement stmtDelete;
    private Statement stmt;
    private StringBuffer insertSql;
    private StringBuffer updateSql;
    private StringBuffer deleteSql;
    private StringBuffer sql;
    private ArrayList<Relatorio> query;
    private Relatorio report;

    public SubrelatorioDAO(Connection conn) throws SQLException {
        insertSql = new StringBuffer("INSERT INTO TB_Relatorio (Titulo, Nome, Arquivo, Id_Relatorio_Mestre, arquivo_jrxml) VALUES (?,?,?,?,?) ");
        updateSql = new StringBuffer("UPDATE TB_Relatorio SET Titulo = ?, Arquivo = ?, arquivo_jrxml=? WHERE ID_Relatorio = ? ");
        deleteSql = new StringBuffer("DELETE FROM TB_Relatorio WHERE ID_Relatorio = ?");
        sql = new StringBuffer();
        query = new ArrayList<Relatorio>();
        stmtInsert = conn.prepareStatement(insertSql.toString());
        stmtUpdate = conn.prepareStatement(updateSql.toString());
        stmtDelete = conn.prepareStatement(deleteSql.toString());
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    public Integer save(Relatorio r) throws SQLException {
        Integer retorno = null;
        if (r.getIdRelatorio() == 0) {
            stmtInsert.setString(1, r.getTitulo());
            stmtInsert.setString(2, r.getNome());
            stmtInsert.setBytes(3, r.getArquivo());
            //stmtInsert.setBinaryStream(3, r.getArquivo(), r.getTamanho().intValue());
            stmtInsert.setInt(4, r.getIdRelatorioMestre().intValue());
            stmtInsert.setBytes(5, r.getArquivo_jrxml());
            //stmtInsert.setBinaryStream(5, r.getArquivo_jrxml(), r.getTamanho_jrxml().intValue());
            retorno = stmtInsert.executeUpdate();
        } else {
            stmtUpdate.setString(1, r.getTitulo());
            stmtUpdate.setBytes(2, r.getArquivo());
            //stmtUpdate.setBinaryStream(2, r.getArquivo(), r.getTamanho().intValue());
            stmtUpdate.setInt(3, r.getIdRelatorio());
            stmtUpdate.setBytes(4, r.getArquivo_jrxml());
            //stmtInsert.setBinaryStream(4, r.getArquivo_jrxml(), r.getTamanho_jrxml().intValue());
            stmtUpdate.setInt(5, r.getIdRelatorioMestre().intValue());
            retorno = stmtUpdate.executeUpdate();
        }
        return retorno;
    }

    public Integer delete(Relatorio r) throws SQLException {
        stmtDelete.setInt(1, r.getIdRelatorio());
        return stmtDelete.executeUpdate();
    }

    public ArrayList<Relatorio> list() throws SQLException {
        sql.delete(0, sql.length());
        sql.append("SELECT r.id_relatorio, r.titulo, r.Nome AS FileName, r.id_relatorio_mestre FROM TB_Relatorio AS R");
        query.clear();
        loadFields(stmt.executeQuery(sql.toString()));
        return query;
    }

    public ArrayList<Relatorio> list(int IdRelatorio) throws SQLException {
        sql.delete(0, sql.length());
        sql.append("SELECT r.id_relatorio, r.titulo, r.Nome AS FileName, r.id_relatorio_mestre FROM TB_Relatorio AS R WHERE R.Id_Relatorio_Mestre=").append(IdRelatorio);
        query.clear();

        loadFields(stmt.executeQuery(sql.toString()));
        return query;
    }

    public Relatorio find(Integer id) throws SQLException {
        sql.delete(0, sql.length());
        sql.append("SELECT r.id_relatorio, r.titulo, r.Nome AS FileName, r.id_relatorio_mestre FROM TB_Relatorio r ")
                .append("WHERE ID_Relatorio = ")
                .append(id);
        //System.out.println(sql.toString());
        query.clear();
        loadFields(stmt.executeQuery(sql.toString()));
        return report;
    }

    private void loadFields(ResultSet rs) throws SQLException {
        report = null;
        ArrayList<Parametro> params = new ArrayList<Parametro>();
        while (rs.next()) {
            if (report == null || report.getIdRelatorio() != rs.getInt("ID_Relatorio")) {
                if (report != null) {
                    report.setParametros(params);
                    query.add(report);
                    params.removeAll(params);
                }
                report = new Relatorio();
                report.setIdRelatorio(rs.getInt("ID_Relatorio"));
                report.setNome(rs.getString("FileName"));
                report.setTitulo(rs.getString("Titulo"));
                //report.setArquivo(rs.getBinaryStream("Arquivo"));
            }
        }
        if (report != null) {
            report.setParametros(params);
            query.add(report);
        }
    }
}
