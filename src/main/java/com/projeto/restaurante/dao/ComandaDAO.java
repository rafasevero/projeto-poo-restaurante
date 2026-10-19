package com.projeto.restaurante.dao;

import com.projeto.restaurante.model.Comanda;
import java.sql.*;

public class ComandaDAO {

    public void salvar(Comanda comanda) throws SQLException {
        // Usando 'valortotal' exatamente como está no seu banco
        String sql = "INSERT INTO comanda (numero_mesa, status, valortotal) VALUES (?, ?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, comanda.getNumeroMesa());
            stmt.setString(2, comanda.getStatus());
            stmt.setDouble(3, comanda.getValorTotal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    comanda.setId(rs.getInt(1));
                }
            }
        }
    }

    public Comanda buscarPorMesa(int numeroMesa) throws SQLException {
        String sql = "SELECT * FROM comanda WHERE numero_mesa = ? AND status = 'ABERTA'";
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, numeroMesa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Comanda(
                            rs.getInt("comanda_id"),
                            rs.getInt("numero_mesa"),
                            rs.getString("status"),
                            null,
                            rs.getDouble("valortotal")
                    );
                }
            }
        }
        return null;
    }

    public void atualizar(Comanda comanda) throws SQLException {
        String sql = "UPDATE comanda SET status = ?, valortotal = ? WHERE comanda_id = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, comanda.getStatus());
            stmt.setDouble(2, comanda.getValorTotal());
            stmt.setInt(3, comanda.getId());
            stmt.executeUpdate();
        }
    }
}