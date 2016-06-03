public class Clazz {
    public Clazz() {
        int k = 5;
        try {
            if (1 < k)
                throw new RuntimeException();
            if (k >7)
                throw new Exception();

        } catch (Exception e) {

        } finally {
            System.out.println(5);
        }
    }
}
