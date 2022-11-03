import java.util.*;
import java.util.stream.Collectors;

public class Aret {
    private String wiadomosc;
    private Scanner strings = new Scanner(System.in);

    public void start(){
        System.out.print("Podaj wiadomosc do zaszyfrowania: ");
        wiadomosc = strings.nextLine();
        System.out.println(zliczWystapienia(wiadomosc));
    }

    private LinkedHashMap<Character, ArrayList<Float>> zliczWystapienia(String ciagZnakow){
        LinkedHashMap<Character, Float> sorted = new LinkedHashMap<>();
        Map <Character, Long> mapStringToCount = ciagZnakow
                .chars().mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        mapStringToCount.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEachOrdered(entry -> sorted.put(entry.getKey(), Float.valueOf(entry.getValue())));

        return wyznaczPrzedzial(sorted);
    }

    private LinkedHashMap<Character, ArrayList<Float>> wyznaczPrzedzial(Map<Character, Float> wystapienia){
        boolean isFirst = true;
        LinkedHashMap<Character, ArrayList<Float>> result = new LinkedHashMap<>();
        for(Map.Entry<Character, Float> iter : wystapienia.entrySet()){
            ArrayList<Float> newPrzedzial = new ArrayList<>();
            if (isFirst){
                newPrzedzial.add((float) 0);
                newPrzedzial.add(iter.getValue()/wiadomosc.length());
                result.put(iter.getKey(), newPrzedzial);
                isFirst=false;
            }else {
                float lastDigit = result.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder())).findFirst().get()
                                .getValue().get(1);

                newPrzedzial.add(lastDigit);
                newPrzedzial.add(lastDigit+(iter.getValue()/wiadomosc.length()));

                result.put(iter.getKey(), newPrzedzial);
            }
        }
        return result;
    }
}
