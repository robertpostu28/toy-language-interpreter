package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.type.Type;

public interface Statement {
    PrgState execute(PrgState state) throws InterpreterException;
    Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException;
}
