import java.util.HashMap;

public class Hasmap2 {
    public static void main(String[] args) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("2", 2);
        hashMap.put("e", 3);
        hashMap.put("t", 4);
        hashMap.put("f", 6);
        hashMap.put("A", 5);
        for (HashMap.Entry<String,Integer> entry : hashMap.entrySet()) {
            System.out.printf("Clave: %s. Valor: %d\n", entry.getKey(),  entry.getValue());
        }
    }
}
