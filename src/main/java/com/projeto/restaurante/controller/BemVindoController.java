package com.projeto.restaurante.controller;
import com.projeto.restaurante.main.GerenciadorTelas;
import javafx.fxml.FXML;
import java.io.IOException;

public class BemVindoController {

    @FXML
    private void irParaLogin() {
        try {
            // Altera para a tela de login na mesma janela
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/login.fxml", "Rafa's Restaurant - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irParaCadastro() {
        try {
            // Altera para a tela de cadastro (usuario.fxml) na mesma janela
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/usuario.fxml", "Rafa's Restaurant - Cadastro");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}