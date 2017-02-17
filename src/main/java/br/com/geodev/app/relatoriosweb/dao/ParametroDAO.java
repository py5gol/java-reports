package br.com.geodev.app.relatoriosweb.dao;

import br.com.geodev.app.relatoriosweb.bean.Parametro;

import java.sql.*;
import java.util.ArrayList;

public class ParametroDAO {
	private PreparedStatement stmtInsert;
	private PreparedStatement stmtUpdate;
	private PreparedStatement stmtDelete;
	private Statement stmt;
	private StringBuffer insertSql;
	private StringBuffer updateSql;
	private StringBuffer deleteSql;
	private StringBuffer sql;
	private ArrayList<Parametro> query;
	private Parametro param;

	public ParametroDAO(Connection conn) throws SQLException {
		insertSql = new StringBuffer("INSERT INTO TB_Relatorio_Parametro (ID_Relatorio, ");
		insertSql.append("Nome, Tipo, Tipo_Html, Label_Html, Classe, Metodo, Propriedade_Chave").append(
				", Propriedade_Descricao) VALUES (?,?,?,?,?,?,?,?,?)");
		updateSql = new StringBuffer("UPDATE TB_Relatorio_Parametro SET Nome = ?, Tipo = ? ");
		updateSql.append(", Tipo_Html = ?, Label_Html = ?, Classe = ?, Metodo = ?, Propriedade_Chave = ? ")
			.append(", Propriedade_Descricao = ? WHERE ID_Parametro = ? ");
		deleteSql = new StringBuffer("DELETE FROM TB_Relatorio_Parametro WHERE ID_Parametro = ?");
		sql = new StringBuffer();
		query = new ArrayList<Parametro>();
		stmtInsert = conn.prepareStatement(insertSql.toString());
		stmtUpdate = conn.prepareStatement(updateSql.toString());
		stmtDelete = conn.prepareStatement(deleteSql.toString());
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	}

	public Integer save(Parametro p) throws SQLException {
		Integer retorno = null;
		if (p.getIdParametro() == 0) {
			stmtInsert.setInt(1, p.getIdRelatorio());
			stmtInsert.setString(2, p.getNome());
			stmtInsert.setString(3, p.getTipo());
			stmtInsert.setString(4, p.getTipoHtml());
			stmtInsert.setString(5, p.getLabelHtml());
			stmtInsert.setString(6, p.getClasse());
			stmtInsert.setString(7, p.getMetodo());
			stmtInsert.setString(8, p.getPropriedadeKey());
			stmtInsert.setString(9, p.getPropriedadeDescricao());
			retorno = stmtInsert.executeUpdate();
		} else {
			stmtUpdate.setString(1, p.getNome());
			stmtUpdate.setString(2, p.getTipo());
			stmtUpdate.setString(3, p.getTipoHtml());
			stmtUpdate.setString(4, p.getLabelHtml());
			stmtUpdate.setString(5, p.getClasse());
			stmtUpdate.setString(6, p.getMetodo());
			stmtUpdate.setString(7, p.getPropriedadeKey());
			stmtUpdate.setString(8, p.getPropriedadeDescricao());
			stmtUpdate.setInt(9, p.getIdParametro());
			retorno = stmtUpdate.executeUpdate();
		}
		return retorno;
	}

	public Integer delete(Parametro p) throws SQLException {
		stmtDelete.setInt(1, p.getIdParametro());
		return stmtDelete.executeUpdate();
	}

	public ArrayList<Parametro> list(Integer id) throws SQLException {
		sql.delete(0, sql.length());
		sql.append("SELECT *, R.Nome AS Relatorio, RP.Nome AS Parametro FROM ")
			.append("TB_Relatorio_Parametro AS RP INNER JOIN TB_Relatorio AS ")
			.append("R USING (ID_Relatorio) WHERE ID_Relatorio = ").append(id);
		query.clear();
		loadFields(stmt.executeQuery(sql.toString()));
		return query;
	}

	public Parametro find(Integer id) throws SQLException {
		sql.delete(0, sql.length());
		sql.append("SELECT *, R.Nome AS Relatorio, RP.Nome AS Parametro FROM ")
			.append("TB_Relatorio_Parametro AS RP INNER JOIN TB_Relatorio AS ")
			.append("R USING (ID_Relatorio) WHERE ID_Parametro = ").append(id);
		query.clear();
		loadFields(stmt.executeQuery(sql.toString()));
		return param;
	}

	private void loadFields(ResultSet rs) throws SQLException {
		param = null;
		while (rs.next()) {
			param = new Parametro();
			param.setIdParametro(rs.getInt("ID_Parametro"));
			param.setIdRelatorio(rs.getInt("ID_Relatorio"));
			param.setNome(rs.getString("Parametro"));
			param.setTipo(rs.getString("Tipo"));
			param.setTipoHtml(rs.getString("Tipo_Html"));
			param.setLabelHtml(rs.getString("Label_Html"));
			param.setClasse(rs.getString("Classe"));
			param.setMetodo(rs.getString("Metodo"));
			param.setNomeRelatorio(rs.getString("Relatorio"));
			param.setPropriedadeKey(rs.getString("Propriedade_Chave"));
			param.setPropriedadeDescricao(rs.getString("Propriedade_Descricao"));
			query.add(param);
		}
	}
}
