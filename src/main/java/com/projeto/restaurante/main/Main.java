package com.projeto.restaurante.main;
import com.projeto.restaurante.main.GerenciadorTelas;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            GerenciadorTelas.setStage(stage);
            stage.setResizable(false);

            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/bem_vindo.fxml", "Rafa's Restaurant - Bem-vindo");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
