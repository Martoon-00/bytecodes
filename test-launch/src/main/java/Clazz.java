public class Clazz {
    int f;

    public static void main(String[] args) {
        new Clazz().a(2);
    }

    public int lol(int a, int b, Integer s) {
        return qwe(b, a, 999);
    }

    public int qwe(int a, int b, int c) {
        return lol(a, b, 3);
    }

    private int a(int p) {
        int s = 1;
        while (s < 5) {
            s = 9;
        }
        System.out.println(s);
        int r = lol(1, 1, null);
        return r;
    }

}
