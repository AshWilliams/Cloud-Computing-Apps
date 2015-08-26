/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea.mp1;


import java.io.File;
import java.lang.reflect.Array;
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
/**
 *
 * @author Cypher
 */
public class MP2 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP2(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
              
        //TODO
        List<String> list = new ArrayList<>();
        List<String> finalList = new ArrayList<>();
        List<String> filterList = new ArrayList<>();
        Integer[] indices = this.getIndexes();
        
        try(BufferedReader br = new BufferedReader(new FileReader(this.inputFileName))) { 
            String line = br.readLine();
            while (line != null) {
                list.add(line);
                line = br.readLine();
            }   
        }        
        for (Integer indice : indices) {
            finalList.add(list.get(indice));
        }
        finalList.stream().map((object) -> new StringTokenizer(object.toLowerCase(), this.delimiters)).forEach((tokens) -> {
            while(tokens.hasMoreTokens()){
                String aux = tokens.nextToken();
                if(!Arrays.asList(this.stopWordsArray).contains(aux)){
                    filterList.add(aux);
                }
            }
        });        
        Set<String> unique = new HashSet<>(filterList);
        Map<String, Integer> map = new HashMap<>();
        unique.stream().forEach((key) -> {
            map.put(key,Collections.frequency(filterList, key));
        });
        ValueComparator<String, Integer> comparator = new ValueComparator<> (map);
        Map<String, Integer> sortedMap = new TreeMap<> (comparator);
        sortedMap.putAll(map);
        int i = 0;
        String[] ret = new String[20];
        for(Map.Entry entry: sortedMap.entrySet()){
            if(i<20)ret[i] = entry.getKey().toString();
            i++;
        }
        return ret;
    }
    
    static class ValueComparator<K, V extends Comparable<V>> implements Comparator<K> {
        Map<K, V> map;
        public ValueComparator(Map<K, V> base) {
            this.map = base;
        }
        @Override
        public int compare(K o1, K o2) {
             return map.get(o2).compareTo(map.get(o1));
        }
    }
    

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
