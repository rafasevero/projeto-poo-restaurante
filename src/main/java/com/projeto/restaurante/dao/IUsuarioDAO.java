package com.projeto.restaurante.dao;
import com.projeto.restaurante.model.Usuario;
import java.sql.SQLException;
import java.util.List;

public interface IUsuarioDAO {
    void insere(Usuario usuario) throws SQLException;
    void atualiza(Usuario usuario) throws SQLException;
    void remove(Usuario usuario) throws SQLException;
    Usuario buscaPorCodigo(int usuarioId) throws SQLException;
    List<Usuario> listaTodos() throws SQLException;
}