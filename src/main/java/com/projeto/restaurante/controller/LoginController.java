package com.projeto.restaurante.controller;
import com.projeto.restaurante.dao.UsuarioDAO;
import com.projeto.restaurante.main.GerenciadorTelas;
import com.projeto.restaurante.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMensagem;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void logarOnAction() {
        String email = txtEmail.getText().trim();
        String senha = txtSenha.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Preencha todos os campos!");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(email, senha);
            if (usuario != null) {
                lblMensagem.setStyle("-fx-text-fill: #006600;");
                lblMensagem.setText("Login bem-sucedido!");

                MenuController.setUsuarioLogado(usuario);

                try {
                    GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/menu_principal.fxml", "Rafa's Restaurant - Menu");
                } catch (IOException e) {
                    lblMensagem.setStyle("-fx-text-fill: #cc0000;");
                    lblMensagem.setText("Erro ao carregar a tela principal: " + e.getMessage());
                }

            } else {
                lblMensagem.setStyle("-fx-text-fill: #cc0000;");
                lblMensagem.setText("E-mail ou senha incorretos.");
            }
        } catch (SQLException e) {
            lblMensagem.setStyle("-fx-text-fill: #cc0000;");
            lblMensagem.setText("Erro ao conectar ao banco: " + e.getMessage());
        }
    }

    @FXML
    private void voltarOnAction() {
        try {
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/bem_vindo.fxml", "Rafa's Restaurant - Bem-vindo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}