package com.projeto.restaurante.dao;
import com.projeto.restaurante.model.Produto;
import java.sql.SQLException;
import java.util.List;

public interface IProdutoDAO {
    void insere(Produto produto) throws SQLException;
    void atualiza(Produto produto) throws SQLException;
    void remove(Produto produto) throws SQLException;
    Produto buscaPorCodigo(int produtoId) throws SQLException;
    List<Produto> listaTodos() throws SQLException;
}