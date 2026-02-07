import java.util.*;
import java.io.*;


//Stress test 1: Natural Language, Machine learning, Artificial Intelligence
//Stress test 2:Digital Transformation, Cloud Computing, etc.

class Entry{
    String key;
    double value;    
    public Entry(String key, double value){
        this.key = key;
        this.value = value;
    }
}

class MostOccured{
    public static HashMap<String, HashMap<String, Integer>> ht = new HashMap<String, HashMap<String, Integer>>();
    public static HashMap<String, Integer> leftSum = new HashMap<String, Integer>();
    public static HashMap<String, Integer> rightSum = new HashMap<String, Integer>();
    public static ArrayList<Entry> pTable = new ArrayList<Entry>();
    public static int wordCount = 0;
    public static void main(String[] args)throws IOException{
        String input = "";
        
        BufferedReader br = new BufferedReader(new FileReader("StressTest.txt"));
        String line = "";
        int r = 0;
        while((line=br.readLine())!=null){
            String[] temp = line.trim().split("\\s+");
            for(int i = 0; i < temp.length-1; i++){
                wordCount++;
                String w1 = temp[i];
                String w2 = temp[i+1];
                if(stopWords.contains(w1) || stopWords.contains(w2))continue;
                ht.putIfAbsent(w1, new HashMap<>());
                HashMap<String, Integer> inner = ht.get(w1);
                inner.put(w2, inner.getOrDefault(w2, 0) + 1);
                leftSum.put(w1, leftSum.getOrDefault(w1, 0) + 1);
                rightSum.put(w2, rightSum.getOrDefault(w2, 0) + 1);
            }
        }
        for(String s : ht.keySet()) {
            for(String x : ht.get(s).keySet()) {
                System.out.println("Key: " + s +  " -- Second Key: " + x + " value" + ht.get(s).get(x));
                if(ht.get(s).get(x) > 5){
                    Entry temp = new Entry(s+" "+x, PMI(s,x));
                    pTable.add(temp);
                }
            }
        }
        //System.err.println(ht.get("a").get("b"));
        pTable.sort((e1, e2) -> Double.compare(e2.value, e1.value));
        for(int i = 0; i < Math.min(pTable.size(), 5); i++){
            System.err.printf("%s: %.3f\n",pTable.get(i).key,pTable.get(i).value);
        }
    }

    public static int coOccurr(String a, String b){
        return ht.get(a).get(b);
    }

    public static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }
    public static double PMI(String a, String b){
        double alpha = 1.0; 
        // Numerator stays true to the data
        double pAB = (double) coOccurr(a,b) / wordCount;
    
        // Denominator is smoothed to prevent low-frequency bias
        double pA = (double) (leftSum.get(a) + alpha) / (wordCount + alpha);
        double pB = (double) (rightSum.get(b) + alpha) / (wordCount + alpha);
    
        return log2(pAB / (pA * pB));
    }

    public static HashSet<String> stopWords = new HashSet<>(Arrays.asList(
        "the", "a", "an", "of", "and", "in", "to", "is", "it", "with", "as", "for", "on", "was", "at", "by"
    ));

}