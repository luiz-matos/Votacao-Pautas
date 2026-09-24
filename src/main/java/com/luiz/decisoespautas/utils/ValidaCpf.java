package com.luiz.decisoespautas.utils;

import java.util.InputMismatchException;

public final class ValidaCpf {

    private ValidaCpf() {
        throw new UnsupportedOperationException("Não há necessidade de instância");
    }

    public static boolean isCPF(String cpf) {
        // considera-se erro CPF"s formados por uma sequencia de numeros iguais
        if (cpf.equals("00000000000") ||
            cpf.equals("11111111111") ||
            cpf.equals("22222222222") || cpf.equals("33333333333") ||
            cpf.equals("44444444444") || cpf.equals("55555555555") ||
            cpf.equals("66666666666") || cpf.equals("77777777777") ||
            cpf.equals("88888888888") || cpf.equals("99999999999") ||
            (cpf.length() != 11))
            return (false);

        char dig10;
        char dig11;
        int soma;
        int contador;
        int restoSoma;
        int num;
        int peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            soma = 0;
            peso = 10;
            for (contador = 0; contador < 9; contador++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere "0" no inteiro 0
                // (48 eh a posicao de "0" na tabela ASCII)
                num = cpf.charAt(contador) - 48;
                soma = soma + (num * peso);
                peso = peso - 1;
            }

            restoSoma = 11 - (soma % 11);
            if ((restoSoma == 10) || (restoSoma == 11))
                dig10 = '0';
            else dig10 = (char) (restoSoma + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            soma = 0;
            peso = 11;
            for (contador = 0; contador < 10; contador++) {
                num = cpf.charAt(contador) - 48;
                soma = soma + (num * peso);
                peso = peso - 1;
            }

            restoSoma = 11 - (soma % 11);
            if ((restoSoma == 10) || (restoSoma == 11))
                dig11 = '0';
            else dig11 = (char) (restoSoma + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
        } catch (InputMismatchException erro) {
            return (false);
        }
    }
}
