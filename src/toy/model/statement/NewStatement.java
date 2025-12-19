package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.adt.Heap;
import toy.model.exp.Exp;
import toy.model.type.RefType;
import toy.model.type.Type;
import toy.model.value.RefValue;
import toy.model.value.Value;

public class NewStatement implements Statement {
    private final String varName;
    private final Exp expression;

    public NewStatement(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        Type varType = typeEnv.lookup(varName);
        Type exprType = expression.typeCheck(typeEnv);

        if (!(varType instanceof RefType)) {
            throw new InterpreterException("NewStatement: variable " + varName + " is not of RefType.");
        }

        if (!((RefType) varType).getInner().equals(exprType)) {
            throw new InterpreterException("NewStatement: type mismatch for variable " + varName + " (expected " + ((RefType) varType).getInner() + ", got " + exprType + ").");
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState prgState) throws InterpreterException {
        Dictionary<String, Value>  symTable = prgState.getSymTable();
        Heap heap = prgState.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new InterpreterException("NewStatement: variable " + varName + " not declared.");
        }

        Value varValue = symTable.lookup(varName);
        Type varType = varValue.getType();

        if (!(varType instanceof RefType)) {
            throw new InterpreterException("NewStatement: variable " + varName + " is not of RefType. Instead found " + varType);
        }

        RefType refType = (RefType) varType;
        Type locationType = refType.getInner();

        Value evlaluatedValue = expression.eval(symTable, heap);

        if (!evlaluatedValue.getType().equals(locationType)) {
            throw new InterpreterException("NewStatement: type mismatch. Variable " + varName + " expects " + locationType + " but expression evaluated to " + evlaluatedValue.getType());
        }

        int newAddress = heap.allocate(evlaluatedValue);

        symTable.update(varName, new RefValue(newAddress, locationType));

        return null;
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + expression.toString() + ")";
    }
}
