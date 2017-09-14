package com.conetex.contract.lang.assignment;

import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.Setable;
import com.conetex.contract.lang.SetableValue;

public class Copy<T> extends AbstractAssigment<T> {

    public static <T> Copy<T> create(SetableValue<T> trg, Accessible<?> src) {
        if (src == null || trg == null) {
            return null;
        }
        Class<T> trgBaseType = trg.getBaseType();
        if (trgBaseType == src.getBaseType()) {
            Accessible<T> srcCasted = src.as(trgBaseType);
            if (srcCasted != null) {
                return new Copy<T>(trg, srcCasted);
            }
        }
        return null;
    }

    public static <T> Copy<T> _create(Setable<T> trg, Accessible<T> src) {
        if (src == null || trg == null) {
            return null;
        }
        return new Copy<T>(trg, src);
    }

    private Copy(Setable<T> trg, Accessible<T> src) {
        super(trg, src);
    }

    @Override
    public boolean doCopy() {
        return true;
    }

}
