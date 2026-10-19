package com.projeto.restaurante.controller;


import com.projeto.restaurante.model.Usuario;
import com.projeto.restaurante.dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class UsuarioController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private TextField txtPerfil;
    @FXML private Label lblMensagem;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void initialize() {
    }

    @FXML
    private void salvarOnAction() {
        try {
            String nome = txtNome.getText();
            String email = txtEmail.getText();
            String senha = txtSenha.getText();
            String perfil = txtPerfil.getText();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                lblMensagem.setText("Preencha todos os campos obrigatórios!");
                return;
            }

            Usuario usuario = new Usuario(0, nome, email, senha, perfil);
            usuarioDAO.insere(usuario);

            lblMensagem.setText("Usuário cadastrado com sucesso! ID: " + usuario.getUsuarioId());
            limparCampos();

        } catch (SQLException e) {
            lblMensagem.setText("Erro no banco de dados: " + e.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtEmail.clear();
        txtSenha.clear();
        txtPerfil.clear();
    }
}