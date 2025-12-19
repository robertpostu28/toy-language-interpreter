package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.adt.Heap;
import toy.model.exp.Exp;
import toy.model.type.RefType;
import toy.model.type.Type;
import toy.model.value.RefValue;
import toy.model.value.Value;

public class WriteHeapStatement implements Statement {
    private final String varName;
    private final Exp expression;

    public WriteHeapStatement(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        Type varType = typeEnv.lookup(varName);
        Type exprType = expression.typeCheck(typeEnv);

        if (!(varType instanceof RefType)) {
            throw new InterpreterException(
                    "wH: variable " + varName + " is not of RefType."
            );
        }

        if (!((RefType) varType).getInner().equals(exprType)) {
            throw new InterpreterException(
                    "wH: type mismatch for variable " + varName +
                            " (expected " + ((RefType) varType).getInner() +
                            ", got " + exprType + ")."
            );
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        Dictionary<String, Value> symTable = state.getSymTable();
        Heap heap = state.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new InterpreterException(
                    "wH: variable " + varName + " is not declared."
            );
        }

        Value varValue = symTable.lookup(varName);
        Type varType = varValue.getType();

        if (!(varType instanceof RefType)) {
            throw new InterpreterException(
                    "wH: variable " + varName +
                            " must be of reference type (got " + varType + ")."
            );
        }

        if (!(varValue instanceof RefValue)) {
            throw new InterpreterException(
                    "wH: variable " + varName +
                            " must store a RefValue at runtime (got " + varValue + ")."
            );
        }

        RefValue refValue = (RefValue) varValue;
        int address = refValue.getAddress();

        if (!heap.isDefined(address)) {
            throw new InterpreterException(
                    "wH: address " + address + " is not allocated in the heap."
            );
        }

        RefType refType = (RefType) varType;
        Type locationType = refType.getInner();

        Value evaluated = expression.eval(symTable, heap);

        if (!evaluated.getType().equals(locationType)) {
            throw new InterpreterException(
                    "wH: type mismatch. Variable " + varName +
                            " expects values of type " + locationType +
                            ", but expression has type " + evaluated.getType() + "."
            );
        }

        heap.update(address, evaluated);

        return null;
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + expression.toString() + ")";
    }
}
