package com.projeto.restaurante.controller;
import com.projeto.restaurante.main.GerenciadorTelas;
import javafx.fxml.FXML;
import java.io.IOException;

public class BemVindoController {

    @FXML
    private void irParaLogin() {
        try {
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/login.fxml", "Rafa's Restaurant - Login");
        } catch (IOException e) {
            e.printStackTrace();
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
}