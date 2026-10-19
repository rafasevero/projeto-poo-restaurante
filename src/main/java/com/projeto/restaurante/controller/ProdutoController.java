package com.projeto.restaurante.controller;
import com.projeto.restaurante.dao.ProdutoDAO;
import com.projeto.restaurante.main.GerenciadorTelas;
import com.projeto.restaurante.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProdutoController {

    @FXML private TextField txtNome;
    @FXML private TextField txtPreco;
    @FXML private ListView<String> listProdutos;
    @FXML private Label lblMensagem;

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private List<Produto> listaProdutosBanco;
    private Produto produtoSelecionado;

    @FXML
    public void initialize() {
        carregarListaProdutos();

        // Quando clicar em um item da lista, preenche os campos automaticamente
        listProdutos.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            if (index >= 0 && index < listaProdutosBanco.size()) {
                produtoSelecionado = listaProdutosBanco.get(index);
                txtNome.setText(produtoSelecionado.getNome());
                txtPreco.setText(String.format("%.2f", produtoSelecionado.getPreco()));
                lblMensagem.setText("");
            }
        });
    }

    private void carregarListaProdutos() {
        try {
            listaProdutosBanco = produtoDAO.listaTodos();
            ObservableList<String> itensExibicao = FXCollections.observableArrayList();

            for (Produto p : listaProdutosBanco) {
                itensExibicao.add(p.getNome() + " - R$ " + String.format("%.2f", p.getPreco()));
            }

            listProdutos.setItems(itensExibicao);
        } catch (SQLException e) {
            exibirErro("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    @FXML
    private void salvarOnAction() {
        String nome = txtNome.getText().trim();
        String precoStr = txtPreco.getText().trim();

        if (nome.isEmpty() || precoStr.isEmpty()) {
            exibirErro("Nome e Preço são obrigatórios!");
            return;
        }

        try {
            double preco = Double.parseDouble(precoStr.replace(",", "."));
            Produto novo = new Produto(nome, preco, 0);
            produtoDAO.insere(novo);

            exibirSucesso("Produto '" + nome + "' cadastrado com sucesso!");
            limparCampos();
            carregarListaProdutos();
        } catch (NumberFormatException e) {
            exibirErro("Digite um preço válido.");
        } catch (SQLException e) {
            exibirErro("Erro no banco: " + e.getMessage());
        }
    }

    @FXML
    private void atualizarOnAction() {
        if (produtoSelecionado == null) {
            exibirErro("Selecione um produto da lista para atualizar!");
            return;
        }

        try {
            String novoNome = txtNome.getText().trim();
            double novoPreco = Double.parseDouble(txtPreco.getText().trim().replace(",", "."));

            produtoSelecionado.setNome(novoNome);
            produtoSelecionado.setPreco(novoPreco);

            produtoDAO.atualiza(produtoSelecionado);
            exibirSucesso("Produto atualizado com sucesso!");
            limparCampos();
            carregarListaProdutos();
        } catch (NumberFormatException e) {
            exibirErro("Preço inválido.");
        } catch (SQLException e) {
            exibirErro("Erro ao atualizar: " + e.getMessage());
        }
    }

    @FXML
    private void deletarOnAction() {
        if (produtoSelecionado == null) {
            exibirErro("Selecione um produto da lista para excluir!");
            return;
        }

        try {
            produtoDAO.remove(produtoSelecionado);
            exibirSucesso("Produto excluído com sucesso!");
            limparCampos();
            carregarListaProdutos();
        } catch (SQLException e) {
            exibirErro("Erro ao excluir: " + e.getMessage());
        }
    }

    @FXML
    private void limparOnAction() {
        limparCampos();
        lblMensagem.setText("");
    }

    private void limparCampos() {
        txtNome.clear();
        txtPreco.clear();
        listProdutos.getSelectionModel().clearSelection();
        produtoSelecionado = null;
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