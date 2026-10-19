package com.projeto.restaurante.controller;

import com.projeto.restaurante.dao.ComandaDAO;
import com.projeto.restaurante.main.GerenciadorTelas;
import com.projeto.restaurante.model.Comanda;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class ComandaController {

    @FXML private TextField txtMesa;
    @FXML private TextField txtStatus;
    @FXML private TextField txtValorTotal;
    @FXML private Label lblMensagem;

    private ComandaDAO comandaDAO = new ComandaDAO();
    private Comanda comandaSelecionada; // Mantém a comanda carregada para atualizar

    @FXML
    private void criarOnAction() {
        String mesaStr = txtMesa.getText().trim();
        String status = txtStatus.getText().trim();
        String valorStr = txtValorTotal.getText().trim();

        if (mesaStr.isEmpty()) {
            exibirErro("Informe ao menos o número da mesa!");
            return;
        }

        try {
            int numeroMesa = Integer.parseInt(mesaStr);
            if (status.isEmpty()) status = "ABERTA";
            double valor = valorStr.isEmpty() ? 0.0 : Double.parseDouble(valorStr);

            Comanda nova = new Comanda(0, numeroMesa, status, null, valor);
            comandaDAO.salvar(nova);

            exibirSucesso("Comanda criada para a Mesa " + numeroMesa + "!");
            limparCampos();
        } catch (NumberFormatException e) {
            exibirErro("Verifique se mesa e valor são números válidos.");
        } catch (SQLException e) {
            exibirErro("Erro no banco: " + e.getMessage());
        }
    }

    @FXML
    private void buscarOnAction() {
        String mesaStr = txtMesa.getText().trim();
        if (mesaStr.isEmpty()) {
            exibirErro("Digite o número da mesa para buscar!");
            return;
        }

        try {
            int numeroMesa = Integer.parseInt(mesaStr);
            comandaSelecionada = comandaDAO.buscarPorMesa(numeroMesa);

            if (comandaSelecionada != null) {
                txtStatus.setText(comandaSelecionada.getStatus());
                txtValorTotal.setText(String.valueOf(comandaSelecionada.getValorTotal()));
                exibirSucesso("Comanda da Mesa " + numeroMesa + " carregada!");
            } else {
                exibirErro("Nenhuma comanda aberta para a Mesa " + numeroMesa);
            }
        } catch (NumberFormatException e) {
            exibirErro("Número de mesa inválido.");
        } catch (SQLException e) {
            exibirErro("Erro na busca: " + e.getMessage());
        }
    }

    @FXML
    private void atualizarOnAction() {
        if (comandaSelecionada == null) {
            exibirErro("Primeiro busque a comanda pela mesa para poder atualizá-la!");
            return;
        }

        try {
            String novoStatus = txtStatus.getText().trim();
            double novoValor = Double.parseDouble(txtValorTotal.getText().trim());

            if (novoStatus.isEmpty()) {
                exibirErro("O status não pode ficar em branco.");
                return;
            }

            comandaSelecionada.setStatus(novoStatus);
            comandaSelecionada.setValorTotal(novoValor);

            comandaDAO.atualizar(comandaSelecionada);

            exibirSucesso("Comanda atualizada com sucesso!");
        } catch (NumberFormatException e) {
            exibirErro("Valor total inválido.");
        } catch (SQLException e) {
            exibirErro("Erro ao atualizar: " + e.getMessage());
        }
    }

    @FXML
    private void limparOnAction() {
        limparCampos();
        lblMensagem.setText("");
    }

    private void limparCampos() {
        txtMesa.clear();
        txtStatus.clear();
        txtValorTotal.clear();
        comandaSelecionada = null;
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
            GerenciadorTelas.trocarTela("/com/projeto/restaurante/view/menu_principal.fxml", "Rafa's Restaurant - Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}