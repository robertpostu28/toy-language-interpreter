package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.value.Value;
import toy.model.type.Type;

public class VariableDeclarationStatement implements Statement {
    private final String varName;
    private final Type varType;

    public VariableDeclarationStatement(String varName, Type varType) {
        this.varName = varName;
        this.varType = varType;
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        if (typeEnv.isDefined(varName)) {
            throw new InterpreterException("Variable already defined in type environment: " + varName);
        }
        typeEnv.update(varName, varType);
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        Dictionary<String, Value> symTable = state.getSymTable();

        if (symTable.isDefined(varName)) {
            throw new InterpreterException("Variable already defined: " + varName);
        }

        Value defaultValue = varType.defaultValue();
        symTable.update(varName, defaultValue);

        return null;
    }

    @Override
    public String toString() {
        return varType + " " + varName;
    }
}
