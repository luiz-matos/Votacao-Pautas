package com.luiz.decisoespautas.utils;

public final class ValidaCpf {

    private ValidaCpf() {
        throw new UnsupportedOperationException("Não há necessidade de instância");
    }

    public static boolean isCPF(String cpf) {
        // Só 11 dígitos, e não todos iguais: 111.111.111-11 passa no cálculo, mas não é um CPF
        if (cpf == null || !cpf.matches("\\d{11}") || cpf.chars().distinct().count() == 1) {
            return false;
        }
        return digitoVerificador(cpf, 9) == cpf.charAt(9) - '0'
            && digitoVerificador(cpf, 10) == cpf.charAt(10) - '0';
    }

    // Soma os primeiros "quantidade" dígitos com pesos decrescentes a partir de quantidade + 1
    private static int digitoVerificador(String cpf, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (cpf.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = 11 - (soma % 11);
        return resto >= 10 ? 0 : resto;
    }
}
