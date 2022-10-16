import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Huffman {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        String ciagZnakow;
        System.out.print("Podaj ciag znakow: ");
        ciagZnakow = cin.nextLine();
        Map<Character, Integer> characterIntegerMap = zliczWystapienia(ciagZnakow);
        System.out.println(characterIntegerMap);
        Map<Character, String> characterStringMap = convertToBinar(characterIntegerMap);
        System.out.println(characterStringMap);
        System.out.println(encryptMessage(characterStringMap, ciagZnakow));

    }

    public static Map<Character, Integer> zliczWystapienia(String ciagZnakow){
        HashSet<Character> removeDuplicates = new HashSet<>();
        Map<Character, Integer> mapStringToCount = new HashMap<>();
        for(int i=0; i< ciagZnakow.length(); i++){
            removeDuplicates.add(ciagZnakow.charAt(i));
        }
        for (Character znak : removeDuplicates){
            int wystapienie = 0 ;
            for (Character comapre : ciagZnakow.toCharArray()){
                if(comapre.equals(znak)){
                    wystapienie++;
                }
            }
            mapStringToCount.put(znak, wystapienie);
        }
        return sort(mapStringToCount);
    }
    
    public static Map<Character, Integer> sort(Map<Character, Integer> mapa){
        List<Map.Entry<Character, Integer>> list = new ArrayList<>(mapa.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<Character, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Character, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static Map<Character, String> convertToBinar(Map<Character, Integer> mapa){
        Map<Character, String> wartoscBinarna = new LinkedHashMap<>();
        int i =0 ;
        String bin = "1";
        for (Character wystapienie: mapa.keySet()){
            if(i == 0){
                wartoscBinarna.put(wystapienie, "0");
            }
            else {
                wartoscBinarna.put(wystapienie, bin.repeat(i) + "0");
            }
            i++;
        }
        return wartoscBinarna;
    }

    public static String encryptMessage(Map<Character, String> wartoscBinarna, String wiadomosc){
        for (Map.Entry<Character, String> szyfr : wartoscBinarna.entrySet()){
            wiadomosc = wiadomosc.replace(szyfr.getKey().toString(), szyfr.getValue());
        }
        return wiadomosc;
    }
}