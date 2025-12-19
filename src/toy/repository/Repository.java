package toy.repository;

import toy.exceptions.InterpreterException;
import toy.model.statement.PrgState;

import java.util.List;

public interface Repository {
    List<PrgState> getProgramsList();
    void setProgramsList(List<PrgState> prgList);

    void logPrgStateExec(PrgState state) throws InterpreterException;
}
