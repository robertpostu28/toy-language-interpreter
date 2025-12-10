package toy.repository;

import toy.exceptions.InterpreterException;
import toy.model.statement.PrgState;

public interface Repository {
    PrgState getProgram();
    void setProgram(PrgState state);

    void logPrgStateExec() throws InterpreterException;
}
