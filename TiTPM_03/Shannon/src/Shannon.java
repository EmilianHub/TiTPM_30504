import java.util.*;
import java.util.stream.Collectors;

public class Shannon {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        String ciagZnakow;
        System.out.print("Podaj ciag znakow: ");
        ciagZnakow = cin.nextLine();
        LinkedHashMap<Character, Long> characterLongMap = zliczWystapienia(ciagZnakow);
        System.out.println(characterLongMap);
        LinkedHashMap<Character, String> binConvert = podzielNaRowne(characterLongMap);
        System.out.println(binConvert);
        /*Map<Character, String> characterStringMap = convertToBinar(characterLongMap);
        System.out.println(characterStringMap);
        String encryptedMessage = encryptMessage(characterStringMap, ciagZnakow);
        System.out.println("Zaszyfrowana wiadomosc: " + encryptedMessage);
        System.out.println("Odszyfrowana wiadomosc: " + decryptMessage(characterStringMap, encryptedMessage));*/
    }

    public static LinkedHashMap<Character, Long> zliczWystapienia(String ciagZnakow){
        Map <Character, Long> mapStringToCount = ciagZnakow
                .chars().mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        return sort(mapStringToCount);
    }

    public static LinkedHashMap<Character, Long> sort(Map<Character, Long> mapa){
        LinkedHashMap<Character, Long> result = new LinkedHashMap<>();
        mapa.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(value -> result.put(value.getKey(), value.getValue()));

        return result;
    }

    public static LinkedHashMap<Character, String> podzielNaRowne(LinkedHashMap<Character, Long> mapa) {
        long sumaPrzod = 0;
        long sumaTyl = 0;
        LinkedHashMap<Long, String> resultMap = new LinkedHashMap<>();
        List<Long> mapValues = mapa.values().stream().collect(Collectors.toList());
        int listSize = mapValues.size()-1;
        for (int i = 0; i < mapValues.size(); i++){
            if(sumaPrzod <= sumaTyl) {
                sumaPrzod += mapValues.get(i);
            }
            else {
                sumaTyl += mapValues.get(listSize);
                listSize--;
                i--;
            }
            if(listSize == i){
                break;
            }
        }
        resultMap.put(sumaPrzod, "0");
        resultMap.put(sumaTyl, "1");
        return podzielNaPodTablice(resultMap, mapa);
    }

    public static LinkedHashMap<Character, String> podzielNaPodTablice(LinkedHashMap<Long, String> mapBin, LinkedHashMap<Character, Long> mapa){
        LinkedHashMap<Character, String> resultMap = new LinkedHashMap<>();
        boolean isFirst = true;
        int sum = 0;
        int inter = 0;
        for(Map.Entry<Character, Long> iterator : mapa.entrySet()){
            inter++;
            Map.Entry<Long, String> bin = mapBin.entrySet().stream().filter(value -> value.getKey() > 0).findFirst().get();
            if(bin.getKey() - sum <= 0){
                mapBin.remove(bin.getKey());
                sum = 0;
                isFirst = true;
                bin = mapBin.entrySet().stream().filter(value -> value.getKey() > 0).findFirst().get();
                inter = 0;
            }else if(inter == 2){
                resultMap.put(iterator.getKey(), bin.getValue()+1);
                mapBin.put(bin.getKey()-sum, bin.getValue()+1);
                mapBin.remove(bin.getKey());
                isFirst = true;
                sum = 0;
                inter = 0;
            }
            else if (isFirst && bin.getKey() - sum > 0){
                sum += sum + iterator.getValue();
                resultMap.put(iterator.getKey(), bin.getValue() + 0);
                isFirst = false;
            } else{
                resultMap.put(iterator.getKey(), bin.getValue()+1);
                sum += iterator.getValue();
                mapBin.replace(bin.getKey(), bin.getValue() + 1);
                isFirst = true;
            }

        }
        return resultMap;
    }

    public static Map<Character, String> convertToBinar(Map<Character, Long> mapa){
        Map<Character, String> wartoscBinarna = new LinkedHashMap<>();
        int i = mapa.size()-1;
        String bin = "1";
        for (Character wystapienie: mapa.keySet()){
            if(i == 0){
                wartoscBinarna.put(wystapienie, "0");
            }
            else {
                wartoscBinarna.put(wystapienie, bin.repeat(i) + "0");
            }
            i--;
        }
        return wartoscBinarna;
    }

    public static String encryptMessage(Map<Character, String> wartoscBinarna, String wiadomosc){
        for (Map.Entry<Character, String> szyfr : wartoscBinarna.entrySet()){
            wiadomosc = wiadomosc.replace(szyfr.getKey().toString(), szyfr.getValue());
        }
        return wiadomosc;
    }

    private static String decryptMessage(Map<Character, String> wartoscBinarna, String encryptedMessage){
        for (Map.Entry<Character, String> szyfr : wartoscBinarna.entrySet()){
            encryptedMessage = encryptedMessage.replace(szyfr.getValue(), szyfr.getKey().toString());
        }
        return encryptedMessage;
    }
}