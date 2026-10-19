package com.projeto.restaurante.controller;
import com.projeto.restaurante.main.GerenciadorTelas;
import com.projeto.restaurante.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;

public class MenuController {

    @FXML private Label lblUsuarioLogado;

    private static Usuario usuarioLogado;

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    @FXML
    public void initialize() {
        if (usuarioLogado != null) {
            lblUsuarioLogado.setText("Olá " + usuarioLogado.getNome() + " (" + usuarioLogado.getPerfil() + ")");
        } else {
            lblUsuarioLogado.setText("Faça login!");
        }
    }

    @FXML
    private void abrirComandas() {
        try {
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/comanda.fxml", "Rafa's Restaurant - Comandas");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirProdutos() {
        try {
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/produto.fxml", "Rafa's Restaurant - Produtos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sairOnAction() {
        try {
            usuarioLogado = null;
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/bem_vindo.fxml", "Rafa's Restaurant - Bem-vindo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirCardapioOnAction() {
        try {
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/cardapio.fxml", "Rafa's Restaurant - Cardápio");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}