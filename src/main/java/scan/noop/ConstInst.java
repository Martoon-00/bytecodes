package scan.noop;

import org.objectweb.asm.Opcodes;
import scan.Cache;
import scan.frame.Frame;
import scan.ref.consts.NullConst;

class ConstInst {
    private final Cache cache;
    private final Frame frame;

    public ConstInst(Cache cache, Frame frame) {
        this.cache = cache;
        this.frame = frame;
    }

    public void apply(int opcode) {
        switch (opcode) {
            case Opcodes.ACONST_NULL:
                frame.pushStack(NullConst.val());
                break;
            case Opcodes.ICONST_M1:
                frame.pushStack(cache.constOf(-1));
                break;
            case Opcodes.ICONST_0:
                frame.pushStack(cache.constOf(0));
                break;
            case Opcodes.ICONST_1:
                frame.pushStack(cache.constOf(1));
                break;
            case Opcodes.ICONST_2:
                frame.pushStack(cache.constOf(2));
                break;
            case Opcodes.ICONST_3:
                frame.pushStack(cache.constOf(3));
                break;
            case Opcodes.ICONST_4:
                frame.pushStack(cache.constOf(4));
                break;
            case Opcodes.ICONST_5:
                frame.pushStack(cache.constOf(5));
                break;
            case Opcodes.LCONST_0:
                frame.pushStack(cache.constOf(0L));
                break;
            case Opcodes.LCONST_1:
                frame.pushStack(cache.constOf(1L));
                break;
            case Opcodes.FCONST_0:
                frame.pushStack(cache.constOf(0F));
                break;
            case Opcodes.FCONST_1:
                frame.pushStack(cache.constOf(1F));
                break;
            case Opcodes.FCONST_2:
                frame.pushStack(cache.constOf(2F));
                break;
            case Opcodes.DCONST_0:
                frame.pushStack(cache.constOf(0.));
                break;
            case Opcodes.DCONST_1:
                frame.pushStack(cache.constOf(1.));
                break;
        }
    }
}
