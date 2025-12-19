package toy.controller;

import toy.exceptions.InterpreterException;
import toy.model.statement.PrgState;

public interface Controller {
    PrgState oneStep(PrgState state) throws InterpreterException;
    void allStep() throws InterpreterException;
    void typeCheckAll() throws InterpreterException;
}
