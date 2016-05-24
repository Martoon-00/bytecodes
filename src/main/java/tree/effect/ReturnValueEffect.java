package tree.effect;

import tree.value.MyValue;

public class ReturnValueEffect {
    private MyValue result;

    public ReturnValueEffect(MyValue result) {
        this.result = result;
    }

    public void simplify() {
        result = result.eliminateRecursion().simplify();
    }
}
