package aston2.module1;

public class DemoMyHashMap {
    public static void main(String[] args) {
        MyHashMap<String, Integer> myMap = new MyHashMap<>();

        myMap.put("Subaru Impreza", 280);
        myMap.put("Lada 2109", 156);
        myMap.put("Mitsubishi Lancer", 250);



        System.out.println(myMap.get("Subaru Impreza"));
        System.out.println(myMap.size());
        System.out.println(myMap.remove("Lada 2109"));
        System.out.println(myMap.size());
        System.out.println(myMap.containsKey("Mitsubishi Lancer"));
        myMap.clear();
        System.out.println(myMap.size());
        System.out.println(myMap.isEmpty());

    }
}
