package com.projeto.restaurante.model;

public class SessaoUsuario {
    private static String perfilLogado;

    public static void setPerfilLogado(String perfil) {
        perfilLogado = perfil;
    }

    public static String getPerfilLogado() {
        return perfilLogado;
    }

    public static void limpar() {
        perfilLogado = null;
    }
}