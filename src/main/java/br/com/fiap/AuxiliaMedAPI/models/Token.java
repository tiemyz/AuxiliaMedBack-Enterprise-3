package br.com.fiap.AuxiliaMedAPI.models;

public record Token(
    String token,
    String type,
    String prefix
) {}