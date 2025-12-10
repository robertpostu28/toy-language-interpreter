package toy.model.statement;

import toy.model.adt.Sequence;
import toy.model.adt.Dictionary;
import toy.model.adt.Stack;
import toy.model.value.StringValue;
import toy.model.value.Value;
import toy.model.statement.Statement;
import toy.model.adt.Heap;

import java.io.BufferedReader;

public class PrgState {
    private final Stack<Statement> exeStack;
    private final Dictionary<String, Value> symTable;
    private final Sequence<Value> out;
    private final Statement originalProgram;
    private final Dictionary<StringValue, BufferedReader> fileTable;
    private final Heap heap;

    public PrgState(Stack<Statement> exeStack,
                    Dictionary<String, Value> symTable,
                    Sequence<Value> out,
                    Dictionary<StringValue, BufferedReader> fileTable,
                    Heap heap,
                    Statement originalProgram) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = originalProgram;

        this.exeStack.push(originalProgram);
    }

    // getters
    public Stack<Statement> getExeStack() {
        return exeStack;
    }
    public Dictionary<String, Value> getSymTable() {
        return symTable;
    }
    public Sequence<Value> getOut() {
        return out;
    }
    public Statement getOriginalProgram() {
        return originalProgram;
    }
    public Dictionary<StringValue, BufferedReader> getFileTable() {return fileTable;}
    public Heap getHeap() { return heap; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ExeStack:\n");
        sb.append(exeStack).append("\n");

        sb.append("SymTable:\n");
        sb.append(symTable).append("\n");

        sb.append("Heap:\n");
        sb.append(heap).append("\n");

        sb.append("Out:\n");
        sb.append(out).append("\n");

        sb.append("FileTable:\n");
        sb.append(fileTable).append("\n");

        return sb.toString();
    }
}
