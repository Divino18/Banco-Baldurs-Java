package util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtil {

    /**
     * Gera o hash MD5 de uma senha em texto plano.
     * @param plainTextPassword A senha a ser codificada.
     * @return A representação hexadecimal de 32 caracteres do hash MD5.
     */
    public static String md5(String plainTextPassword) {
        if (plainTextPassword == null) {
            return null; // Ou lançar uma exceção, dependendo do comportamento desejado
        }
        try {
            // Cria uma instância do MessageDigest para o algoritmo MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Calcula o hash da senha
            byte[] messageDigest = md.digest(plainTextPassword.getBytes());

            // Converte o array de bytes para uma representação em BigInteger
            BigInteger no = new BigInteger(1, messageDigest);

            // Converte o BigInteger para uma string hexadecimal
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext; // Retorno do hash
        } catch (NoSuchAlgorithmException e) {

            System.err.println("Erro: Algoritmo MD5 não encontrado - " + e.getMessage());
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Valida se uma senha atende aos critérios de força definidos.
     * Critérios:
     * - Mínimo de 8 caracteres
     * - Pelo menos 1 letra maiúscula
     * - Pelo menos 1 letra minúscula
     * - Pelo menos 1 número
     * - Pelo menos 1 caractere especial (ex: !@#$%^&*)
     * @param password A senha a ser validada.
     * @return true se a senha for forte, false caso contrário.
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false; // Mínimo de 8 caracteres
        }

        // Regex para verificar se contém pelo menos uma letra maiúscula
        Pattern maiusculaPattern = Pattern.compile("[A-Z]");
        Matcher temMaiuscula = maiusculaPattern.matcher(password);
        if (!temMaiuscula.find()) {
            return false; // Precisa de letra maiúscula
        }

        // Regex para verificar se contém pelo menos uma letra minúscula
        Pattern minusculaPattern = Pattern.compile("[a-z]");
        Matcher temMinuscula = minusculaPattern.matcher(password);
        if (!temMinuscula.find()) {
            return false; // Precisa de letra minúscula
        }

        // Regex para verificar se contém pelo menos um número
        Pattern numeroPattern = Pattern.compile("[0-9]");
        Matcher temNumero = numeroPattern.matcher(password);
        if (!temNumero.find()) {
            return false; // Precisa de número
        }

        // Regex para verificar se contém pelo menos um caractere especial
        Pattern especialPattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]");
        Matcher temEspecial = especialPattern.matcher(password);
        if (!temEspecial.find()) {
            return false; // Precisa de caractere especial
        }

        return true; // Passou em todas as validações
    }
}