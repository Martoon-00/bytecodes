public class Clazz {
    static int f;

    public static void main(String[] args) {
        myMethod(2, 3);
    }

    public static void myMethod(int a, int n) {
        int r = 1;
        for (int i = 0; i < n; i++) {
            r *= a;
        }
        System.out.println(r);
    }

}
