package com.projeto.restaurante.controller;

import com.projeto.restaurante.dao.UsuarioDAO;
import com.projeto.restaurante.main.GerenciadorTelas;
import com.projeto.restaurante.model.SessaoUsuario;
import com.projeto.restaurante.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMensagem;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void entrarOnAction() {
        String email = txtEmail.getText().trim();
        String senha = txtSenha.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            exibirErro("Preencha o e-mail e a senha!");
            return;
        }

        try {
            List<Usuario> usuarios = usuarioDAO.listaTodos();
            Usuario usuarioAutenticado = null;

            for (Usuario u : usuarios) {
                if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                    usuarioAutenticado = u;
                    break;
                }
            }

            if (usuarioAutenticado != null) {
                // Salva o perfil na sessão global para controlar os acessos no menu
                SessaoUsuario.setPerfilLogado(usuarioAutenticado.getPerfil());

                // Vai para o menu principal
                GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/menu_principal.fxml", "Rafa's Restaurant - Menu Principal");
            } else {
                exibirErro("E-mail ou senha incorretos!");
            }

        } catch (SQLException e) {
            exibirErro("Erro ao conectar ao banco: " + e.getMessage());
        } catch (IOException e) {
            exibirErro("Erro ao carregar a tela principal: " + e.getMessage());
        }
    }

    @FXML
    private void irParaCadastro() {
        try {
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/usuario.fxml", "Rafa's Restaurant - Cadastro");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void voltarOnAction() {
        try {
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/bem_vindo.fxml", "Rafa's Restaurant");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exibirErro(String msg) {
        lblMensagem.setStyle("-fx-text-fill: #cc0000; -fx-font-weight: bold;");
        lblMensagem.setText(msg);
    }
}