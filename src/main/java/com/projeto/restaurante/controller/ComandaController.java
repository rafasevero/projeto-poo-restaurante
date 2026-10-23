package com.projeto.restaurante.controller;

import com.projeto.restaurante.dao.ComandaDAO;
import com.projeto.restaurante.dao.ItemComandaDAO;
import com.projeto.restaurante.dao.ProdutoDAO;
import com.projeto.restaurante.main.GerenciadorTelas;
import com.projeto.restaurante.model.Comanda;
import com.projeto.restaurante.model.ItemComanda;
import com.projeto.restaurante.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ComandaController {

    @FXML private TextField txtMesa;
    @FXML private TextField txtStatus;
    @FXML private TextField txtValorTotal;
    @FXML private TextField txtQuantidade;
    @FXML private ListView<String> listComandas;
    @FXML private ListView<String> listItensComanda;
    @FXML private ComboBox<Produto> cbProdutos;
    @FXML private Label lblMensagem;

    private ComandaDAO comandaDAO = new ComandaDAO();
    private ItemComandaDAO itemComandaDAO = new ItemComandaDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    private List<Comanda> listaComandasBanco;
    private List<Produto> listaProdutosBanco;
    private List<ItemComanda> listaItensBanco;
    private Comanda comandaSelecionada;

    @FXML
    public void initialize() {
        carregarListaComandas();
        carregarProdutosComboBox();

        // Listener ao selecionar uma comanda
        listComandas.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            if (index >= 0 && index < listaComandasBanco.size()) {
                comandaSelecionada = listaComandasBanco.get(index);
                txtMesa.setText(String.valueOf(comandaSelecionada.getNumeroMesa()));
                txtStatus.setText(comandaSelecionada.getStatus());
                carregarItensComandaSelecionada();
                lblMensagem.setText("");
            }
        });
    }

    private void carregarListaComandas() {
        try {
            listaComandasBanco = comandaDAO.listaTodos();
            ObservableList<String> exibicao = FXCollections.observableArrayList();

            for (Comanda c : listaComandasBanco) {
                double total = itemComandaDAO.calculaValorTotal(c.getComandaId());
                exibicao.add("Mesa " + c.getNumeroMesa() + " | Status: " + c.getStatus() + " | Total: R$ " + String.format("%.2f", total));
            }

            listComandas.setItems(exibicao);
        } catch (SQLException e) {
            exibirErro("Erro ao carregar comandas: " + e.getMessage());
        }
    }

    private void carregarProdutosComboBox() {
        try {
            listaProdutosBanco = produtoDAO.listaTodos();
            ObservableList<Produto> produtosObs = FXCollections.observableArrayList(listaProdutosBanco);
            cbProdutos.setItems(produtosObs);
        } catch (SQLException e) {
            exibirErro("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void carregarItensComandaSelecionada() {
        if (comandaSelecionada == null) return;

        try {
            listaItensBanco = itemComandaDAO.listaPorComanda(comandaSelecionada.getComandaId());
            ObservableList<String> exibicaoItens = FXCollections.observableArrayList();

            for (ItemComanda item : listaItensBanco) {
                double subtotal = item.getQuantidade() * item.getPrecoUnitario();
                exibicaoItens.add(item.getQuantidade() + "x " + item.getNomeProduto() + " (R$ " + String.format("%.2f", item.getPrecoUnitario()) + " un) -> Subtotal: R$ " + String.format("%.2f", subtotal));
            }

            listItensComanda.setItems(exibicaoItens);

            // Atualiza o valor total automaticamente
            double totalGeral = itemComandaDAO.calculaValorTotal(comandaSelecionada.getComandaId());
            txtValorTotal.setText(String.format("%.2f", totalGeral));

        } catch (SQLException e) {
            exibirErro("Erro ao carregar itens da comanda: " + e.getMessage());
        }
    }

    @FXML
    private void adicionarItemOnAction() {
        if (comandaSelecionada == null) {
            exibirErro("Selecione uma comanda na lista acima primeiro!");
            return;
        }

        Produto produtoEscolhido = cbProdutos.getSelectionModel().getSelectedItem();
        if (produtoEscolhido == null) {
            exibirErro("Selecione um produto do cardápio!");
            return;
        }

        String qtdStr = txtQuantidade.getText().trim();
        if (qtdStr.isEmpty()) {
            qtdStr = "1"; // padrão 1
        }

        try {
            int quantidade = Integer.parseInt(qtdStr);
            if (quantidade <= 0) {
                exibirErro("A quantidade deve ser maior que zero.");
                return;
            }

            ItemComanda novoItem = new ItemComanda(
                    comandaSelecionada.getComandaId(),
                    produtoEscolhido.getProdutoId(),
                    quantidade,
                    produtoEscolhido.getPreco()
            );

            itemComandaDAO.insere(novoItem);
            exibirSucesso("Produto adicionado à comanda!");
            txtQuantidade.clear();
            cbProdutos.getSelectionModel().clearSelection();

            // Recarrega os itens e atualiza o total e a listagem geral
            carregarItensComandaSelecionada();
            carregarListaComandas();

        } catch (NumberFormatException e) {
            exibirErro("Quantidade inválida.");
        } catch (SQLException e) {
            exibirErro("Erro ao adicionar item: " + e.getMessage());
        }
    }

    @FXML
    private void criarOnAction() {
        String mesaStr = txtMesa.getText().trim();
        String status = txtStatus.getText().trim();

        if (mesaStr.isEmpty() || status.isEmpty()) {
            exibirErro("Número da Mesa e Status são obrigatórios!");
            return;
        }

        try {
            int mesa = Integer.parseInt(mesaStr);
            Comanda nova = new Comanda(mesa, status);
            comandaDAO.insere(nova);

            exibirSucesso("Comanda aberta com sucesso!");
            limparCampos();
            carregarListaComandas();
        } catch (NumberFormatException e) {
            exibirErro("O número da mesa deve ser um número inteiro.");
        } catch (SQLException e) {
            exibirErro("Erro no banco: " + e.getMessage());
        }
    }

    @FXML
    private void atualizarOnAction() {
        if (comandaSelecionada == null) {
            exibirErro("Selecione uma comanda da lista para atualizar!");
            return;
        }

        try {
            int mesa = Integer.parseInt(txtMesa.getText().trim());
            String status = txtStatus.getText().trim();

            comandaSelecionada.setNumeroMesa(mesa);
            comandaSelecionada.setStatus(status);

            comandaDAO.atualiza(comandaSelecionada);
            exibirSucesso("Comanda atualizada com sucesso!");
            limparCampos();
            carregarListaComandas();
        } catch (NumberFormatException e) {
            exibirErro("Número da mesa inválido.");
        } catch (SQLException e) {
            exibirErro("Erro ao atualizar: " + e.getMessage());
        }
    }

    @FXML
    private void deletarOnAction() {
        if (comandaSelecionada == null) {
            exibirErro("Selecione uma comanda da lista para excluir!");
            return;
        }

        try {
            comandaDAO.remove(comandaSelecionada);
            exibirSucesso("Comanda excluída com sucesso!");
            limparCampos();
            carregarListaComandas();
        } catch (SQLException e) {
            exibirErro("Erro ao excluir (verifique se há itens vinculados).");
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
        txtQuantidade.clear();
        cbProdutos.getSelectionModel().clearSelection();
        listComandas.getSelectionModel().clearSelection();
        listItensComanda.getItems().clear();
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