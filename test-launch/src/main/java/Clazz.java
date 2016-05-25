public class Clazz {
    int f;

    public Clazz(int[] array) {
        f = 6;
        for (int i = 0; i < 10; i++) {
            f = i;
        }
    }

    private Clazz a(int k, int s) {
        return this;
    }

}
