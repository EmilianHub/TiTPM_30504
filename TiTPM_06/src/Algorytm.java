import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Algorytm {
    private String wiadomosc;
    private Scanner scanner = new Scanner(System.in);
    private String pattern ="____";
    private String buffor = "";
    private String mapKey;
    private Integer wielkoscSlownika;
    private LinkedHashMap<String, String> mapaPodstawowa = new LinkedHashMap<>();
    private LinkedList<Integer> encryptedMessage = new LinkedList<>();
    private Integer indexStart;
    private Integer indexEnd;

    public void zapiszWiadomosc(){
        System.out.print("Podaj wiadmosc: ");
        wiadomosc = scanner.nextLine();

        System.out.println("Podaj wielkość slownika: ");
        wielkoscSlownika = scanner.nextInt();

        slowinkPodstawowy();
    }

    private void slowinkPodstawowy() {
        char firstLetter = wiadomosc.charAt(0);

        mapKey = String.format("(0, 0, %s)", firstLetter);
        mapaPodstawowa.put(mapKey, pattern);

        pattern = pattern.substring(1) + firstLetter;
        rozszerzSlownik();
    }

    private void rozszerzSlownik(){
        for(int j = 1; j<=wiadomosc.length()+wielkoscSlownika; j++){
            if(j >= wiadomosc.length()-1){
                buffor = String.valueOf(wiadomosc.charAt(wiadomosc.length()-1));
                if (conatinsValue(buffor)) {
                    mapKey = String.format("(%d, %d, %s)", indexStart, indexEnd, "_");
                    mapaPodstawowa.put(mapKey, pattern);
                }
            }else {
                if(buffor.length() == 0){
                    buffor = wiadomosc.substring(j, j+4);
                }else {
                    pattern = pattern.substring(indexEnd+1) + buffor.substring(0, indexEnd+1);
                    buffor = buffor.substring(indexEnd+1) + wiadomosc.substring(j, j+indexEnd+1);
                }
                mapaPodstawowa.put("#", pattern);
                if (conatinsValue(buffor)){
                    mapaPodstawowa.remove("#");
                    char firstLetter = buffor.charAt(indexEnd);
                    mapKey = String.format("(%d, %d, %s)", indexStart, indexEnd, firstLetter);
                    mapaPodstawowa.put(mapKey, pattern);
                    if(indexEnd + 1 == wielkoscSlownika){
                        j += indexEnd-1;
                    }else {
                        j += indexEnd + 1;
                    }
                }
                else {
                    char firstLetter = buffor.charAt(0);
                    mapKey = String.format("(0, 0, %s)", firstLetter);
                    pattern = pattern.substring(1) + firstLetter;
                    mapaPodstawowa.put(mapKey, pattern);
                    buffor = "";
                }
            }
        }
        czytajSzyfr();
    }

    private void czytajSzyfr(){
        for (String index : mapaPodstawowa.keySet()){
            System.out.print(index + " ");
        }
    }

    /*public void zapiszDoPlikuONazwie() {
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
    }*/

    /*public void odczytajZPliku(){
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
    }*/

    private boolean conatinsValue(String buffor){
        int bufforSize = wielkoscSlownika/2;
        LinkedList<String> slownikValues = mapaPodstawowa.values().stream().collect(Collectors.toCollection(LinkedList::new));
        for(int s=slownikValues.size()-1; s>=0; s--){
            String simpleWord = slownikValues.get(s).replaceAll("_", "");
            if(buffor.length()==1){
                if(simpleWord.contains(buffor)){
                    indexStart = slownikValues.get(s).indexOf(buffor);
                    indexEnd = 1;
                    return true;
                }
                return false;
            }
            for(int i =0; i<2; i++) {
                String bufSub = buffor.substring(i, i + 3);
                if(simpleWord.contains(bufSub)){
                    indexStart = slownikValues.get(s).indexOf(bufSub);
                    indexEnd = 3;
                    return true;
                }
            }

            for(int i =0; i<=buffor.length()-bufforSize; i++){
                String bufSub = buffor.substring(i, i + 2);
                if(simpleWord.contains(bufSub)){
                    indexStart = slownikValues.get(s).indexOf(bufSub);
                    indexEnd = 2;
                    return true;
                }
            }
        }
        return false;
    }
}

