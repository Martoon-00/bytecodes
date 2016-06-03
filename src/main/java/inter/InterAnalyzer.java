package inter;

import scan.MethodRef;
import tree.effect.MethodCallEffect;
import tree.value.MethodParamValue;
import tree.value.MyValue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InterAnalyzer {
    private final InterContext context;

    public InterAnalyzer(InterContext context) {
        this.context = context;
    }

    public Optional<List<MyValue>> analyzePassedParams(MethodRef method, int depth, boolean remainReferences) {
//        Type[] argumentTypes = Type.getArgumentTypes(method.getDesc());
        // TODO: maybe find a better way to get parameters?
        Optional<List<MyValue>> maybeParams = context.getCallEffects(method).stream()
                .findAny()
                .map(MethodCallEffect::getParams);
        if (!maybeParams.isPresent())
            return Optional.empty();
        List<MyValue> params = maybeParams.get();

        return Stream.iterate(0, i -> i + 1).limit(params.size())
                .map(i -> new MethodParamValue(method, i, params.get(i).getType()))
                .map(param -> resolve(param, depth, remainReferences))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of));
    }

    private MyValue resolve(MyValue value, int depth, boolean remainReferences) {
        MyValue result = value
                .resolveReferences(context, depth)
                .eliminateRecursion();
        if (!remainReferences)
            result = result.eliminateReferences();
        return result.simplify();
    }
}
