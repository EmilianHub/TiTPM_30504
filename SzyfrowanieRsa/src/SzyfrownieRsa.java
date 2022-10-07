import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class SzyfrownieRsa {
    public static void main(String[] args){
        Scanner cin = new Scanner(System.in);
        System.out.println("Podaj tekst do zaszyfowania: ");
        String password = cin.nextLine();
        ArrayList<Integer> asciiArray = convertToAscii(password);
        System.out.println("Zakodowane: " + asciiArray);
        System.out.println("Odkodowane: " + decryptWithPrivateKey(asciiArray));
    }

    private static ArrayList<Integer> convertToAscii(String password){
        ArrayList<Integer> asciiArray = new ArrayList<>();
        for(int i = 0; i<password.length(); i++){
            asciiArray.add((int) password.toUpperCase().charAt(i));
        }
        return encryptWithRsa(asciiArray);
    }

    private static ArrayList<Integer> encryptWithRsa(ArrayList<Integer> asciiArray){
        int k = 391;
        long s = 3;
        ArrayList<Integer> encryptedPassword = new ArrayList<>();
        for (Integer password:asciiArray) {
            double enc = Math.pow(password, s);
            encryptedPassword.add(((int)enc%k));
        }
        return encryptedPassword;
    }

    private static String decryptWithPrivateKey(ArrayList<Integer> encryptedPassword){
        int d = 235;
        BigInteger k = new BigInteger("391");
        ArrayList<Integer> decryptedPassword = new ArrayList<>();
        for (Integer password:encryptedPassword){
            BigInteger dec = new BigInteger(String.valueOf(password));
            dec = dec.pow(d).mod(k);
            decryptedPassword.add(dec.intValue());
        }
        return convertToString(decryptedPassword);
    }

    private static String convertToString(ArrayList<Integer> decryptedPassword){
        StringBuilder password = new StringBuilder();
        for(Integer ascii:decryptedPassword){
            password.append((char) ascii.intValue());
        }
        return password.toString();
    }
}
