public class Clazz {
    public static void main(String[] args) {
        System.out.println(new Clazz().a());
    }

    private int a() {
        long k = 5;
        return b(4, 6, (byte) k).length();
    }

    private String b(int a, int b, byte c) {
        return a + " - " + b;
    }
}
