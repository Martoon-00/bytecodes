public class Clazz {
    public static void main(String[] args) {
        System.out.println(new Clazz().a());
    }

    private boolean a() {
        String lol = "qwe";
        boolean p = lol instanceof Object;
        return p;
    }

//    private String b(int a, int b, byte c) {
//        return a + " - " + b;
//    }
}
