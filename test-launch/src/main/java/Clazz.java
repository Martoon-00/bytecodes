public class Clazz {
    int f;

//    public static void main(String[] args) {
//        a(2);
//    }

    public static int myMethod(Integer a) {
        return 5;
    }

    private static int a(int p) {
        int s = 1;
        for (int i = 0; i < 10; i++) {
            s = 2;
        }
        return myMethod(s);
    }

}
