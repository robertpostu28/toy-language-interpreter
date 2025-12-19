package toy.model.statement;

import toy.exceptions.InterpreterException;
import toy.model.adt.RuntimeDictionary;
import toy.model.adt.RuntimeStack;
import toy.model.adt.Dictionary;
import toy.model.adt.Stack;
import toy.model.value.Value;
import toy.model.type.Type;

import java.util.function.BiConsumer;

public class ForkStatement implements Statement {
    private final Statement statement;

    public ForkStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        // fork creates a new thread with the same type environment
        statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState prgState) throws InterpreterException {
        // new execution stack for the forked program
        Stack<Statement> newExeStack = new RuntimeStack<>();

        // copy the symbol table for the forked program
        Dictionary<String, Value> newSymTable = new RuntimeDictionary<>();
        prgState.getSymTable().asMap().forEach(newSymTable::update);

        return new PrgState(
                newExeStack,
                newSymTable,
                prgState.getOut(),
                prgState.getFileTable(),
                prgState.getHeap(),
                statement
        );
    }

    @Override
    public String toString() {
        return "fork(" + statement + ")";
    }
}
