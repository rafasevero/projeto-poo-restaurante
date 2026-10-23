package com.projeto.restaurante.controller;
import com.projeto.restaurante.dao.CardapioDAO;
import com.projeto.restaurante.dao.ProdutoDAO;
import com.projeto.restaurante.main.GerenciadorTelas;
import com.projeto.restaurante.model.Cardapio;
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

public class CardapioController {

    @FXML private TextField txtNomeSecao;
    @FXML private TextField txtDescricao;
    @FXML private ComboBox<String> cbProdutos;
    @FXML private ListView<String> listCardapio;
    @FXML private Label lblMensagem;

    private CardapioDAO cardapioDAO = new CardapioDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    private List<Cardapio> listaCardapioBanco;
    private List<Produto> listaProdutosBanco;
    private Cardapio itemSelecionado;

    @FXML
    public void initialize() {
        carregarProdutosCombo();
        carregarListaCardapio();

        listCardapio.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            if (index >= 0 && index < listaCardapioBanco.size()) {
                itemSelecionado = listaCardapioBanco.get(index);
                txtNomeSecao.setText(itemSelecionado.getNomeSecao());
                txtDescricao.setText(itemSelecionado.getDescricao() != null ? itemSelecionado.getDescricao() : "");
                selecionarProdutoNoCombo(itemSelecionado.getProdutoId());
                lblMensagem.setText("");
            }
        });
    }

    private void carregarProdutosCombo() {
        try {
            listaProdutosBanco = produtoDAO.listaTodos();
            ObservableList<String> itens = FXCollections.observableArrayList();
            for (Produto p : listaProdutosBanco) {
                itens.add(p.getProdutoId() + " - " + p.getNome());
            }
            cbProdutos.setItems(itens);
        } catch (SQLException e) {
            exibirErro("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void carregarListaCardapio() {
        try {
            listaCardapioBanco = cardapioDAO.listaTodos();
            ObservableList<String> exibicao = FXCollections.observableArrayList();

            for (Cardapio c : listaCardapioBanco) {
                // Descobre o nome do produto através do ID associado
                String nomeProduto = "Produto não encontrado";
                if (listaProdutosBanco != null) {
                    for (Produto p : listaProdutosBanco) {
                        if (p.getProdutoId() == c.getProdutoId()) {
                            nomeProduto = p.getNome();
                            break;
                        }
                    }
                }

                String descText = (c.getDescricao() != null && !c.getDescricao().isEmpty()) ? " - " + c.getDescricao() : "";
                exibicao.add("[" + c.getNomeSecao() + "] " + nomeProduto + descText);
            }

            listCardapio.setItems(exibicao);
        } catch (SQLException e) {
            exibirErro("Erro ao carregar cardápio: " + e.getMessage());
        }
    }

    private void selecionarProdutoNoCombo(int produtoId) {
        for (int i = 0; i < listaProdutosBanco.size(); i++) {
            if (listaProdutosBanco.get(i).getProdutoId() == produtoId) {
                cbProdutos.getSelectionModel().select(i);
                break;
            }
        }
    }

    @FXML
    private void salvarOnAction() {
        String secao = txtNomeSecao.getText().trim();
        String desc = txtDescricao.getText().trim();
        int selectedIndex = cbProdutos.getSelectionModel().getSelectedIndex();

        if (secao.isEmpty() || selectedIndex < 0) {
            exibirErro("Seção e Seleção do Produto são obrigatórias!");
            return;
        }

        try {
            int produtoId = listaProdutosBanco.get(selectedIndex).getProdutoId();
            Cardapio novo = new Cardapio(secao, desc, produtoId);
            cardapioDAO.insere(novo);

            exibirSucesso("Item adicionado ao cardápio com sucesso!");
            limparCampos();
            carregarListaCardapio();
        } catch (SQLException e) {
            exibirErro("Erro no banco: " + e.getMessage());
        }
    }

    @FXML
    private void atualizarOnAction() {
        if (itemSelecionado == null) {
            exibirErro("Selecione um item da lista para atualizar!");
            return;
        }

        int selectedIndex = cbProdutos.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            exibirErro("Selecione um produto associado!");
            return;
        }

        try {
            itemSelecionado.setNomeSecao(txtNomeSecao.getText().trim());
            itemSelecionado.setDescricao(txtDescricao.getText().trim());
            itemSelecionado.setProdutoId(listaProdutosBanco.get(selectedIndex).getProdutoId());

            cardapioDAO.atualiza(itemSelecionado);
            exibirSucesso("Item do cardápio atualizado!");
            limparCampos();
            carregarListaCardapio();
        } catch (SQLException e) {
            exibirErro("Erro ao atualizar: " + e.getMessage());
        }
    }

    @FXML
    private void deletarOnAction() {
        if (itemSelecionado == null) {
            exibirErro("Selecione um item para excluir!");
            return;
        }

        try {
            cardapioDAO.remove(itemSelecionado);
            exibirSucesso("Item removido do cardápio!");
            limparCampos();
            carregarListaCardapio();
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
        txtNomeSecao.clear();
        txtDescricao.clear();
        cbProdutos.getSelectionModel().clearSelection();
        listCardapio.getSelectionModel().clearSelection();
        itemSelecionado = null;
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