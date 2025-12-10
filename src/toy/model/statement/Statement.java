package toy.model.statement;

import toy.exceptions.InterpreterException;

public interface Statement {
    PrgState execute(PrgState state) throws InterpreterException;
}
