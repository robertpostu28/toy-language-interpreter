package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.exp.Exp;
import toy.model.type.StringType;
import toy.model.type.Type;
import toy.model.value.StringValue;
import toy.model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStatement implements Statement {
    private final Exp exp;

    public CloseRFileStatement(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        Type expType = exp.typeCheck(typeEnv);

        if (!expType.equals(new StringType())) {
            throw new InterpreterException("closeRFile: expression is not of type string.");
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        Dictionary<String, Value> symTable = state.getSymTable();
        Dictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        Value val = exp.eval(symTable, state.getHeap());
        if (!val.getType().equals(new StringType())) {
            throw new InterpreterException("closeRFile: expression is not of type string.");
        }

        StringValue fileNameVal = (StringValue) val;
        if (!fileTable.isDefined(fileNameVal)) {
            throw new InterpreterException("closeRFile: file " + fileNameVal.getValue() + " is not open.");
        }

        BufferedReader br = fileTable.lookup(fileNameVal);
        try {
            br.close();
        } catch (IOException e) {
            throw new InterpreterException("closeRFile: error closing file " + fileNameVal.getValue() + ": " + e.getMessage());
        }

        fileTable.remove(fileNameVal);
        return null;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}
