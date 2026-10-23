package com.projeto.restaurante.controller;

import com.projeto.restaurante.dao.UsuarioDAO;
import com.projeto.restaurante.main.GerenciadorTelas;
import com.projeto.restaurante.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UsuarioController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private ComboBox<String> cbPerfil; // Mapeado corretamente do FXML
    @FXML private ListView<String> listUsuarios;
    @FXML private Label lblMensagem;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private List<Usuario> listaUsuariosBanco;
    private Usuario usuarioSelecionado;

    @FXML
    public void initialize() {
        // Preenche o ComboBox com os cargos
        cbPerfil.setItems(FXCollections.observableArrayList(
                "Gerente",
                "Garçom",
                "Cozinheiro"
        ));

        carregarUsuarios();

        if (listUsuarios != null) {
            listUsuarios.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
                int index = newVal.intValue();
                if (index >= 0 && index < listaUsuariosBanco.size()) {
                    usuarioSelecionado = listaUsuariosBanco.get(index);
                    txtNome.setText(usuarioSelecionado.getNome());
                    txtEmail.setText(usuarioSelecionado.getEmail());
                    txtSenha.setText(usuarioSelecionado.getSenha());
                    cbPerfil.setValue(usuarioSelecionado.getPerfil());
                    lblMensagem.setText("");
                }
            });
        }
    }

    private void carregarUsuarios() {
        try {
            listaUsuariosBanco = usuarioDAO.listaTodos();
            ObservableList<String> exibicao = FXCollections.observableArrayList();

            for (Usuario u : listaUsuariosBanco) {
                exibicao.add(u.getNome() + " (" + u.getEmail() + ") - Cargo: " + u.getPerfil());
            }

            if (listUsuarios != null) {
                listUsuarios.setItems(exibicao);
            }
        } catch (SQLException e) {
            exibirErro("Erro ao carregar usuários: " + e.getMessage());
        }
    }

    @FXML
    private void salvarOnAction() {
        try {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String senha = txtSenha.getText().trim();
            String perfil = cbPerfil.getSelectionModel().getSelectedItem();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                exibirErro("Preencha todos os campos obrigatórios!");
                return;
            }

            if (perfil == null || perfil.isEmpty()) {
                exibirErro("Por favor, selecione um cargo!");
                return;
            }

            Usuario novoUsuario = new Usuario(nome, email, senha, perfil);
            usuarioDAO.insere(novoUsuario);

            try {
                GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/login.fxml", "Rafa's Restaurant - Login");
            } catch (IOException e) {
                exibirErro("Erro ao carregar a tela de login: " + e.getMessage());
            }

        } catch (SQLException e) {
            exibirErro("Erro no banco de dados: " + e.getMessage());
        }
    }

    @FXML
    private void limparOnAction() {
        limparCampos();
        lblMensagem.setText("");
    }

    private void limparCampos() {
        txtNome.clear();
        txtEmail.clear();
        txtSenha.clear();
        cbPerfil.getSelectionModel().clearSelection();
        if (listUsuarios != null) {
            listUsuarios.getSelectionModel().clearSelection();
        }
        usuarioSelecionado = null;
    }

    private void exibirSucesso(String msg) {
        lblMensagem.setStyle("-fx-text-fill: #006600; -fx-font-weight: bold;");
        lblMensagem.setText(msg);
    }

    private void exibirErro(String msg) {
        lblMensagem.setStyle("-fx-text-fill: #cc0000; -fx-font-weight: bold;");
        lblMensagem.setText(msg);
    }

    @FXML
    private void voltarOnAction() {
        try {
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/bem_vindo.fxml", "Rafa's Restaurant");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}