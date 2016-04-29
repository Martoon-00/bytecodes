package util;

import util.ref.consts.Const;

import static util.RefType.OBJECTREF;

public class Cache {
    public Byte get(Byte k) {
        return k;
    }

    public Short get(Short k) {
        return k;
    }

    public Character get(Character k) {
        return k;
    }

    public Integer get(Integer k) {
        return k;
    }

    public Long get(Long k) {
        return k;
    }

    public Float get(Float k) {
        return k;
    }

    public Double get(Double k) {
        return k;
    }

    public String get(String k) {
        return k;
    }

    public Const constOf(byte k) {
        return new Const(get(k), RefType.BYTE);
    }

    public Const constOf(short k) {
        return new Const(get(k), RefType.SHORT);
    }

    public Const constOf(int k) {
        return new Const(get(k), RefType.INT);
    }

    public Const constOf(long k) {
        return new Const(get(k), RefType.LONG);
    }

    public Const constOf(float k) {
        return new Const(get(k), RefType.FLOAT);
    }

    public Const constOf(double k) {
        return new Const(get(k), RefType.DOUBLE);
    }

    public Const constOf(String k) {
        return new Const(get(k), OBJECTREF);
    }


    public Object get(Object v, RefType type) {
        switch (type) {
            case BYTE:
                return get((byte) v);
            case SHORT:
                return get((short) v);
            case CHAR:
                return get((char) v);
            case BOOLEAN:
            case INT:
                return get((int) v);
            case LONG:
                return get((long) v);
            case FLOAT:
                return get((float) v);
            case DOUBLE:
                return get((double) v);
            case OBJECTREF:
                return v;
            default:
                throw new RuntimeException("Unknown type: " + type);
        }
    }

    public Const constOf(Object k, RefType type) {
        return new Const(get(k, type), type);
    }
}
