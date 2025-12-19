package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.exp.Exp;
import toy.model.type.StringType;
import toy.model.type.Type;
import toy.model.value.StringValue;
import toy.model.value.Value;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenRFileStatement implements Statement {
    private final Exp exp;

    public OpenRFileStatement(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        Type expType = exp.typeCheck(typeEnv);

        if (!expType.equals(new StringType())) {
            throw new InterpreterException("openRFile: expression is not of type string.");
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        Dictionary<String, Value> symTable = state.getSymTable();
        Dictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        Value val = exp.eval(symTable, state.getHeap());
        if (!val.getType().equals(new StringType())) {
            throw new InterpreterException("openRFile: expression is not of type string.");
        }

        StringValue fileNameVal = (StringValue) val;
        if (fileTable.isDefined(fileNameVal)) {
            throw new InterpreterException("openRFile: file " + fileNameVal.getValue() + " is already open.");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileNameVal.getValue()));
            fileTable.update(fileNameVal, br);
        } catch (IOException e) {
            throw new InterpreterException("openRFile: could not open file " + fileNameVal.getValue() + ": " + e.getMessage());
        }

        return null;
    }

    @Override
    public String toString() {
        return "openRFile(" + exp + ")";
    }
}
