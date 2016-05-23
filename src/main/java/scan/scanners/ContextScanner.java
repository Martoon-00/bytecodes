package scan.scanners;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import scan.frame.Frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ContextScanner<F extends Frame> extends MethodVisitor {
    private int instNum;
    private final List<F> frames = new ArrayList<>();
    private final Function<F, F> defaultNextFrame;
    private final Map<Label, Integer> labels = new HashMap<>();

    public ContextScanner(F initialFrame, Function<F, F> defaultNextFrame) {
        super(Opcodes.ASM4);
        frames.add(initialFrame);
        this.defaultNextFrame = defaultNextFrame;
    }

    public int curInstIndex() {
        return instNum - 1;
    }

    private void registerInst() {
        frames.add(defaultNextFrame.apply(getCurFrame()));
        instNum++;
    }

    public F getCurFrame() {
        return frames.get(instNum);
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
        registerInst();
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand);
        registerInst();
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        registerInst();
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
        registerInst();
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);
        registerInst();
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        super.visitMethodInsn(opcode, owner, name, desc);
        registerInst();
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
        registerInst();
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        super.visitJumpInsn(opcode, label);
        registerInst();
    }

    @Override
    public void visitLdcInsn(Object cst) {
        super.visitLdcInsn(cst);
        registerInst();
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        super.visitIincInsn(var, increment);
        registerInst();
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        super.visitTableSwitchInsn(min, max, dflt, labels);
        registerInst();
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        super.visitLookupSwitchInsn(dflt, keys, labels);
        registerInst();
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        super.visitMultiANewArrayInsn(desc, dims);
        registerInst();
    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);
        registerInst();
        labels.put(label, curInstIndex());
    }
}