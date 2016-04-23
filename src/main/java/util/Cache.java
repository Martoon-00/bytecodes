package util;

import util.ref.consts.Const;

public class Cache {
    public Byte get(Byte k) {
        return k;
    }

    public Short get(Short k) {
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
        return new Const(get(k));
    }

    public Const constOf(short k) {
        return new Const(get(k));
    }

    public Const constOf(int k) {
        return new Const(get(k));
    }

    public Const constOf(long k) {
        return new Const(get(k));
    }

    public Const constOf(float k) {
        return new Const(get(k));
    }

    public Const constOf(double k) {
        return new Const(get(k));
    }

    public Const constOf(String k) {
        return new Const(get(k));
    }


}
