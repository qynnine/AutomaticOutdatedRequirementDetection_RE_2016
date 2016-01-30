package exp.iTrust;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by niejia on 15/12/20.
 */
public class CosineSimilarity {

    static double cosine_similarity(Map<String, Double> v1, Map<String, Double> v2) {
        Set<String> both = new HashSet(v1.keySet());
        both.retainAll(v2.keySet());
        double sclar = 0, norm1 = 0, norm2 = 0;
        for (String k : both) sclar += v1.get(k) * v2.get(k);
        for (String k : v1.keySet()) norm1 += v1.get(k) * v1.get(k);
        for (String k : v2.keySet()) norm2 += v2.get(k) * v2.get(k);
        return sclar / Math.sqrt(norm1 * norm2);
    }


    public static void main(String[] args) {
        Map<String, Double> query = new HashMap<>();
        query.put("niejia", 0.08902092841634837);
        query.put("weiwei", 0.46209812037329684);
        Map<String, Double> document = new HashMap<>();
        document.put("niejia", 0.06676569631226129);
        document.put("weiwei", 0.6931471805599453);
        System.out.println(cosine_similarity(query, document));
    }

}
