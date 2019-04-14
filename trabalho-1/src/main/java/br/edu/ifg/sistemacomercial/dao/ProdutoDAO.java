package br.edu.ifg.sistemacomercial.dao;

import br.edu.ifg.sistemacomercial.entity.Categoria;
import br.edu.ifg.sistemacomercial.entity.Produto;
import br.edu.ifg.sistemacomercial.util.FabricadeConexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void salvar(Produto entity) throws SQLException{
        //Ordem das colunas: id, nome, marca, codigo_barras, unidade_medida, categoria_id
        String sqlInsert = "insert into produto (id, nome, marca, codigo_barras, unidade_medida, categoria_id"
                + ") values (default, ?, ?, ?, ?, ?)";
        String sqlUpdate = "update usuario set nome = ?, marca = ?, codigo_barras = ?, unidade_medida = ?, categoria_id = ?"
                + " where id = ?";
        
        PreparedStatement  ps;
        if(entity.getId() == null){
            ps = FabricadeConexao.getConexao().prepareStatement(sqlInsert);
        } else {
            ps = FabricadeConexao.getConexao().prepareStatement(sqlUpdate);
            ps.setLong(5, entity.getId());
        }
        ps.setString(1, entity.getNome());
        ps.setString(2, entity.getMarca());
        ps.setString(3, entity.getCodigoBarras());
        ps.setString(4, entity.getUnidadeMedida());
        ps.setInt(5, entity.getCategoria().getId());
        
        
        FabricadeConexao.getConexao().setAutoCommit(false);
        ps.execute();
        FabricadeConexao.getConexao().commit();
        FabricadeConexao.fecharConexao();
    }
    
    public void deletar(Produto entity) throws SQLException{
        String sqlDelete = "delete from produto where id = ?";
        PreparedStatement ps = FabricadeConexao.getConexao().prepareStatement(sqlDelete);
        ps.setLong(1, entity.getId());
        ps.execute();
        FabricadeConexao.fecharConexao();
    }
    
    public List<Produto> listar() throws SQLException{
        String sqlQuery = "select p.id, p.nome, p.marca, p.codigo_barras,"
                + " p.unidade_medida, c.id, c.nome from produto as p join categoria as c"
                + "on = c.id = p.categoria_id";
        PreparedStatement ps = FabricadeConexao.getConexao().prepareStatement(sqlQuery);
        //java.sql.ResultSet
        ResultSet rs = ps.executeQuery();
        List<Produto> produtos = new ArrayList<>();
        while(rs.next()){
            Produto produto = new Produto();
            produto.setId(rs.getInt("p.id"));
            produto.setNome(rs.getString("p.nome"));
            produto.setMarca(rs.getString("p.marca"));
            produto.setCodigoBarras(rs.getString("p.codigo_barras"));
            produto.setUnidadeMedida(rs.getString("p.unidade_medida"));
            
            Categoria categoria = new Categoria();
     
            categoria.setNome(rs.getString("c.nome"));
            
            
            produtos.add(produto);
        }
        FabricadeConexao.fecharConexao();
        return produtos;
    }
}
