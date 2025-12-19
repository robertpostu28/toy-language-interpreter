package toy.model.exp;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.adt.Heap;
import toy.model.type.RefType;
import toy.model.type.Type;
import toy.model.value.RefValue;
import toy.model.value.Value;

public class ReadHeapExp implements Exp {
    private final Exp expression;

    public ReadHeapExp(Exp exp) {
        this.expression = exp;
    }

    @Override
    public Type typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        Type exprType = expression.typeCheck(typeEnv);

        if (exprType instanceof RefType refType) {
            return refType.getInner();
        } else {
            throw new InterpreterException("ReadHeapExp: expression " + expression + " not of RefType, got: " + exprType);
        }
    }

    @Override
    public Value eval(Dictionary<String, Value> symTable, Heap heap) throws InterpreterException {
        Value value = expression.eval(symTable, heap);

        if (!(value instanceof RefValue refValue)) {
            throw new InterpreterException("ReadHeapExp: expression " + expression + " not a RefValue, got: " + value.getType());
        }

        RefValue ref = (RefValue) value;
        int address = ref.getAddress();

        if (!heap.isDefined(address)) {
            throw new InterpreterException("ReadHeapExp: address " + address + " not defined in heap");
        }

        return heap.lookup(address);
    }

    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }
}
