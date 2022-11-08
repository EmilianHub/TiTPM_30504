import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Algorytm {
    private String wiadomosc;
    private Scanner scanner = new Scanner(System.in);
    private String pattern ="";
    private Integer i = 0;
    private LinkedHashMap<Integer, String> mapaPodstawowa = new LinkedHashMap<>();
    private LinkedList<Integer> encryptedMessage = new LinkedList<>();

    public void zapiszWiadomosc(){
        System.out.print("Podaj wiadmosc: ");
        wiadomosc = scanner.nextLine();

        slowinkPodstawowy();
    }
    private void slowinkPodstawowy(){
        List<Character> collect = wiadomosc.chars().mapToObj(o -> (char) o).distinct().collect(Collectors.toList());
        Collections.sort(collect);

        for(Character value : collect){
            i++;
            mapaPodstawowa.put(i, value.toString());
        }
        rozszerzSlownik();
    }

    private void rozszerzSlownik(){
        for(int j = 0; j<=wiadomosc.length()-1; j++){
            if(j == wiadomosc.length()-1){
                String lastChar = String.valueOf(wiadomosc.charAt(j));
                List<Integer> lastPattern = mapaPodstawowa.entrySet().stream()
                        .filter(v -> v.getValue().equals(lastChar))
                        .map(v -> v.getKey()).collect(Collectors.toList());
                encryptedMessage.add(lastPattern.get(0));
            }else {
                pattern += wiadomosc.substring(j, j + 2);
                if (mapaPodstawowa.containsValue(pattern)) {
                    int indexToConcat = pattern.length() - 1;
                    pattern = pattern.substring(0, indexToConcat);
                } else {
                    int index = pattern.length() - 1;
                    String shortedMessage = pattern.substring(0, index);
                    List<Integer> indexPattern = mapaPodstawowa.entrySet().stream()
                            .filter(v -> v.getValue().equals(shortedMessage))
                            .map(v -> v.getKey()).collect(Collectors.toList());
                    encryptedMessage.add(indexPattern.get(0));
                    i++;
                    mapaPodstawowa.put(i, pattern);

                    pattern = "";
                }
            }
        }
        czytajSzyfr();
    }

    private void czytajSzyfr(){
        for (Integer index : encryptedMessage){
            System.out.print(index + " ");
        }
    }

    public void zapiszDoPlikuONazwie() {
        if (mapaPodstawowa.isEmpty()){
            System.out.println("\nSłownik jest pusty\nZapis do pliku przerwany");
            return;
        }
        try{
            System.out.print("\nPodaj nazwe pliku: ");
            String nazwaPliku = scanner.nextLine();
            File file = new File(nazwaPliku);
            if(!file.exists()){
                System.out.println("Plik zostal utworzony");
                file.createNewFile();
            }
            if(file.canWrite()) {
                FileWriter fileWriter = new FileWriter(file);
                Formatter formatter = new Formatter(fileWriter);

                for (Map.Entry<Integer, String> mapka : mapaPodstawowa.entrySet()){
                    formatter.format("%d | %s\r\n", mapka.getKey(), mapka.getValue());
                }
                formatter.close();
                fileWriter.close();
            }
            System.out.println("Plik zostal zapisany");
        }catch (Exception e){
            System.out.println("Wystapil problem podczas zapisu do pliku");
            System.out.println(e.getMessage());
        }
    }

    public void decryptMessage(){
        String decryptedMessage = "";
        for(Integer index : encryptedMessage){
            decryptedMessage += mapaPodstawowa.get(index);
        }
        System.out.println("\nOdszyfrowna wiadomosc: " + decryptedMessage);
    }

    public void odczytajZPliku(){
        try{
            mapaPodstawowa.clear();
            System.out.print("Podaj nazwe pliku: ");
            String nazwaPliku = scanner.nextLine();
            File file = new File(nazwaPliku);
            String odczytZpliku;
            if (file.exists()){
                Scanner fileScanner = new Scanner(file);
                while(fileScanner.hasNextLine()){
                    odczytZpliku = fileScanner.nextLine();
                    String[] split = odczytZpliku.split("[|]\s");
                    int index = Integer.parseInt(split[0].replace(" ", ""));
                    String values = split[1].replace(" ", "");
                    mapaPodstawowa.put(index, values);
                }
                fileScanner.close();
            }
            else{
                System.out.println("Plik nie istnieje");
            }
        }catch (Exception e){
            System.out.println("Wystapil blad podczas odczytu pliku");
        }
    }
}

