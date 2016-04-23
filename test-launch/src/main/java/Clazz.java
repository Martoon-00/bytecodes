public class Clazz {
    public static void main(String[] args) {
        System.out.println(new Clazz().a());
    }

    private int a() {
        double k = 2;
        int l = 3;
        return b(4, 6, k < l).length();
    }

    private String b(int a, int b, boolean c) {
        return a + " - " + b;
    }
}
