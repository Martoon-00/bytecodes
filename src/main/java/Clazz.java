public class Clazz {
    static int f;

    public int foo(int i, int j) {
        int a = 1;
        int b = 2;
        while (a < 30) {
            int t = a;
            a = b;
            b = t;
        }
        myMethod(a);
        return 5;
    }

    public static void myMethod(int a) {
        System.out.println();
    }

}
