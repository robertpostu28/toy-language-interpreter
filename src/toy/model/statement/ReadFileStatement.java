package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.exp.Exp;
import toy.model.type.IntType;
import toy.model.type.StringType;
import toy.model.value.IntValue;
import toy.model.value.StringValue;
import toy.model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements Statement {
    private final Exp expFile;
    private final String varName;

    public ReadFileStatement(Exp expFile, String varName) {
        this.expFile = expFile;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        Dictionary<String, Value> symTable = state.getSymTable();
        Dictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        if (!symTable.isDefined(varName)) {
            throw new InterpreterException("readFile: variable " + varName + " is not declared.");
        }

        Value varVal = symTable.lookup(varName);
        if (!varVal.getType().equals(new IntType())) {
            throw new InterpreterException("readFile: variable " + varName + " is not of type int.");
        }

        Value val = expFile.eval(symTable, state.getHeap());
        if (!val.getType().equals(new StringType())) {
            throw new InterpreterException("readFile: expression is not of type string.");
        }

        StringValue fileNameVal = (StringValue) val;
        if (!fileTable.isDefined(fileNameVal)) {
            throw new InterpreterException("readFile: file " + fileNameVal.getValue() + " is not open.");
        }

        BufferedReader br = fileTable.lookup(fileNameVal);
        try {
            String line = br.readLine();
            int intVal;
            if (line == null) {
                intVal = 0;
            } else {
                try {
                    intVal = Integer.parseInt(line.trim());
                } catch (NumberFormatException e) {
                    throw new InterpreterException("readFile: cannot parse integer from line: '" + line + "'.");
                }
            }
            symTable.update(varName, new IntValue(intVal));
        } catch (IOException e) {
            throw new InterpreterException("readFile: error reading from file " + fileNameVal.getValue() + ": " + e.getMessage());
        }

        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + expFile + ", " + varName + ")";
    }
}
