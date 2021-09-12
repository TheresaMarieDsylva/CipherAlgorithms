package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Yay! Start your cryptography..");

        System.out.println("*******************************PLAYFAIR CIPHER***********************************");
        String secretKey = "SECURITY";
        String plaintext = "MEET AT MILITARY HOUSE";
        Character keyMatrix[][] = generateKeyMatrix(secretKey);

        String cipherText = generateCipherText(plaintext, keyMatrix);
        System.out.println(cipherText);

        String decryptText = generatePlainText(cipherText, keyMatrix);
        System.out.println(decryptText);

        System.out.println("***************************MATRIX TRANSPOSITION***********************************");
        String matrixCipherText = generateMatrixCipher("I like cryptography", new int[]{4, 3, 2, 1, 5});
        System.out.println(matrixCipherText);

        String matrixPlainText = generateMatrixPlain(matrixCipherText, new int[]{4, 3, 2, 1, 5});
        System.out.println(matrixPlainText);
    }

    private static Character[][] generateKeyMatrix(String secretKey) {
        Character[][] keyMatrix = new Character[5][5];
        List<Character> keyLst = new ArrayList<>();
        Character[] alphaCharacters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        List<Character> alphaLst = new ArrayList<>(Arrays.asList(alphaCharacters));

        String[] secArr = secretKey.split(" ");

        StringBuilder strBuilder = new StringBuilder();
        for (String str : secArr) {
            strBuilder.append(str);
        }

        secretKey = strBuilder.toString();

        Set<String> secretKeySet = new LinkedHashSet<>();
        for (int s = 0; s < secretKey.length(); s++) {
            secretKeySet.add(Character.toString(secretKey.charAt(s)));
        }
        StringBuilder secretKeyBuilder = new StringBuilder();
        for (String s : secretKeySet) {
            secretKeyBuilder.append(s);
        }
        secretKey = secretKeyBuilder.toString();

        for (int i = 0; i < secretKey.length(); i++) {
            Character key = secretKey.charAt(i);
            if (alphaLst.contains(key)) {
                alphaLst.remove(key);
            }
        }

        for (int i = 0; i < secretKey.length(); i++) {
            keyLst.add(secretKey.charAt(i));
        }
        alphaLst.forEach(a -> {
            keyLst.add(a);
        });

        int i = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                keyMatrix[row][col] = keyLst.get(i);
                i += 1;
            }
        }

        for (int row = 0; row < keyMatrix.length; row++) {
            for (int col = 0; col < keyMatrix[row].length; col++) {
                System.out.print(keyMatrix[row][col] + " ");
            }
            System.out.println();
        }

        return keyMatrix;
    }

    private static String generateCipherText(String plaintext, Character keyMatrix[][]) {
        String repeatedLetter = "X";
        String paddedLetter = "Z";
        List<String> dividedStr = new ArrayList<>();

        plaintext = plaintext.replaceAll(" ", "");

        //Divide into two, check for repeated, include X and store as string
        for (int i = 0; i < plaintext.length(); i += 2) {
            int size = plaintext.length();
            String first = Character.toString(plaintext.charAt(i));
            String next = i < size - 1 ? Character.toString(plaintext.charAt(i + 1)) : "";
            if (first.equalsIgnoreCase(next)) {
                String firstHalf = plaintext.substring(0, i + 1);
                String secondHalf = plaintext.substring(i + 1, size);
                plaintext = firstHalf.concat(repeatedLetter).concat(secondHalf);
            }
        }

        //Check if the text is even and append with padded letter
        if (plaintext.length() % 2 != 0) {
            plaintext = plaintext.concat(paddedLetter);
        }

        //Divide into two, check for repeated and store as string
        for (int i = 0; i < plaintext.length(); i += 2) {
            String initial = Character.toString(plaintext.charAt(i));
            String last = Character.toString(plaintext.charAt(i + 1));
            dividedStr.add(initial + last);
        }

        //Loop through matrix and find the matching value and save it to another list
        List<String> encrypted = new ArrayList<>();

        dividedStr.forEach(d -> {
            String initial;
            String last;
            int initialRowPosition = -1;
            int initialColPosition = -1;
            int lastRowPosition = -1;
            int lastColPosition = -1;
            String[] eachPair = d.split("");
            for (int row = 0; row < keyMatrix.length; row++) {
                for (int col = 0; col < keyMatrix[row].length; col++) {
                    if (eachPair[0].equalsIgnoreCase(Character.toString(keyMatrix[row][col]))) {
                        initialRowPosition = row;
                        initialColPosition = col;
                    }
                    if (eachPair[1].equalsIgnoreCase(Character.toString(keyMatrix[row][col]))) {
                        lastRowPosition = row;
                        lastColPosition = col;
                    }
                }
            }
            if (initialColPosition == lastColPosition) {
                int initialRowKey = initialRowPosition == 4 ? 0 : initialRowPosition + 1;
                int lastRowKey = lastRowPosition == 4 ? 0 : lastRowPosition + 1;
                initial = Character.toString(keyMatrix[initialRowKey][initialColPosition]);
                last = Character.toString(keyMatrix[lastRowKey][lastColPosition]);
            } else if (initialRowPosition == lastRowPosition) {
                int initialColKey = initialColPosition == 4 ? 0 : initialColPosition + 1;
                int lastColKey = lastColPosition == 4 ? 0 : lastColPosition + 1;
                initial = Character.toString(keyMatrix[initialRowPosition][initialColKey]);
                ;
                last = Character.toString(keyMatrix[lastRowPosition][lastColKey]);
            } else {
                initial = Character.toString(keyMatrix[initialRowPosition][lastColPosition]);
                ;
                last = Character.toString(keyMatrix[lastRowPosition][initialColPosition]);
            }
            encrypted.add(initial + last);
        });

        //Form a String from the new list
        StringBuilder encryptedText = new StringBuilder();
        for (String each : encrypted) {
            encryptedText.append(each);
        }

        String cipherText = encryptedText.toString();
        return cipherText;
    }

    private static String generatePlainText(String cipherText, Character[][] keyMatrix) {
        List<String> dividedStr = new ArrayList<>();
        String plainText = "";

        //Divide into two, and store as string
        for (int i = 0; i < cipherText.length(); i += 2) {
            String initial = Character.toString(cipherText.charAt(i));
            String last = Character.toString(cipherText.charAt(i + 1));
            dividedStr.add(initial + last);
        }

        List<String> decrypted = new ArrayList<>();
        dividedStr.forEach(d -> {
            String initial = "";
            String last = "";
            int initialRowPosition = -1, initialColPosition = -1;
            int lastRowPosition = -1, lastColPosition = -1;
            String[] eachPair = d.split("");
            for (int row = 0; row < keyMatrix.length; row++) {
                for (int col = 0; col < keyMatrix[row].length; col++) {
                    if (eachPair[0].equalsIgnoreCase(Character.toString(keyMatrix[row][col]))) {
                        initialRowPosition = row;
                        initialColPosition = col;
                    }
                    if (eachPair[1].equalsIgnoreCase(Character.toString(keyMatrix[row][col]))) {
                        lastRowPosition = row;
                        lastColPosition = col;
                    }
                }
            }
            if (initialColPosition == lastColPosition) {
                int initialRowKey = initialRowPosition == 0 ? 4 : initialRowPosition - 1;
                int lastRowKey = lastRowPosition == 0 ? 4 : lastRowPosition - 1;
                initial = Character.toString(keyMatrix[initialRowKey][initialColPosition]);
                last = Character.toString(keyMatrix[lastRowKey][lastColPosition]);
            } else if (initialRowPosition == lastRowPosition) {
                int initialColKey = initialColPosition == 0 ? 4 : initialColPosition - 1;
                int lastColKey = lastColPosition == 0 ? 4 : lastColPosition - 1;
                initial = Character.toString(keyMatrix[initialRowPosition][initialColKey]);
                last = Character.toString(keyMatrix[lastRowPosition][lastColKey]);
            } else {
                initial = Character.toString(keyMatrix[initialRowPosition][lastColPosition]);
                last = Character.toString(keyMatrix[lastRowPosition][initialColPosition]);
            }
            decrypted.add(initial + last);
        });

        StringBuilder decryptedText = new StringBuilder();
        for (String each : decrypted) {
            decryptedText.append(each);
        }
        plainText = decryptedText.toString();

        List<String> temp = new ArrayList<>();
        for (int i = 0; i < plainText.length(); i += 2) {
            String initial = Character.toString(plainText.charAt(i));
            String last = Character.toString(plainText.charAt(i + 1));
            temp.add(initial + last);
        }

        decryptedText = new StringBuilder();
        for (String each : temp) {
            decryptedText.append(each);
        }

        plainText = decryptedText.toString();

        return plainText;
    }

    private static String generateMatrixCipher(String plainText, int[] key) {

        String[] pArr = plainText.split(" ");
        for (int i = 0; i < pArr.length - 1; i++) {
            pArr[i] = pArr[i].concat("%");
        }

        StringBuilder strBuilder = new StringBuilder();
        for (String str : pArr) {
            strBuilder.append(str);
        }

        plainText = strBuilder.toString();
        int colSize = key.length;
        int plainTxtLen = plainText.length();
        double rowSize = Math.ceil((double) plainTxtLen / (double) colSize);

        String[][] matrixCipher = new String[(int) rowSize][colSize];
        int i = 0;
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                matrixCipher[row][col] = i < plainTxtLen ? Character.toString(plainText.charAt(i)) : "%";
                i += 1;
            }
        }

        StringBuilder cipherBuilder = new StringBuilder();
        for (int colKey : key) {
            for (int row = 0; row < matrixCipher.length; row++) {
                cipherBuilder.append(matrixCipher[row][colKey - 1]);
            }
        }

        return cipherBuilder.toString();
    }

    private static String generateMatrixPlain(String cipherText, int[] key) {
        int colSize = key.length;
        int rowSize = cipherText.length() / colSize;

        String[][] matrixCipher = new String[rowSize][colSize];

        int i = 0;
        for (int colKey : key) {
            for (int row = 0; row < rowSize; row++) {
                matrixCipher[row][colKey - 1] = i < cipherText.length() ? Character.toString(cipherText.charAt(i)) : "";
                i += 1;
            }
        }
        StringBuilder plainTextBuilder = new StringBuilder();
        for (int row = 0; row < matrixCipher.length; row++) {
            for (int col = 0; col < matrixCipher[row].length; col++) {
                plainTextBuilder.append(matrixCipher[row][col]);
            }
        }

        return plainTextBuilder.toString();
    }
}
