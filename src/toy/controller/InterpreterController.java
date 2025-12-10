package toy.controller;

import toy.exceptions.InterpreterException;
import toy.model.adt.Stack;
import toy.model.statement.PrgState;
import toy.model.statement.Statement;
import toy.model.value.RefValue;
import toy.model.value.Value;
import toy.repository.Repository;

import java.util.*;
import java.util.stream.Collectors;

public class InterpreterController implements Controller {
    private final Repository repo;

    public InterpreterController(Repository repo) {
        this.repo = repo;
    }

    @Override
    public PrgState oneStep(PrgState state) throws InterpreterException {
        Stack<Statement> stack =state.getExeStack();
        if (stack.isEmpty())
            throw new InterpreterException("Cannot execute oneStep: Execution stack is empty!");
        Statement currentStatement = stack.pop();
        return currentStatement.execute(state);
    }

    @Override
    public void allStep() throws InterpreterException {
        PrgState state = repo.getProgram();
        if (state == null) {
            throw new InterpreterException("No program found in the repository.");
        }

        // log the initial state before any step
        repo.logPrgStateExec();

        while (!state.getExeStack().isEmpty()) {
            oneStep(state); // execute one step

            Map<Integer, Value> newHeap =
                    safeGarbageCollector(
                            state.getSymTable().asMap().values(),
                            state.getHeap().getContent()
                    );

            state.getHeap().setContent(newHeap);

            repo.logPrgStateExec(); // log after each step
        }
    }

    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .collect(Collectors.toList());
    }

    private Set<Integer> computeReachableAddresses(Collection<Value> symTableValues,
                                                   Map<Integer, Value> heap) {
        Set<Integer> reachable = new HashSet<>(getAddrFromSymTable(symTableValues));
        Deque<Integer> worklist = new ArrayDeque<>(reachable);

        while (!worklist.isEmpty()) {
            int addr = worklist.pop();
            Value v = heap.get(addr);
            if (v instanceof RefValue refVal) {
                int innerAddr = refVal.getAddress();
                if (!reachable.contains(innerAddr) && heap.containsKey(innerAddr)) {
                    reachable.add(innerAddr);
                    worklist.push(innerAddr);
                }
            }
        }

        return reachable;
    }

    private Map<Integer, Value> safeGarbageCollector(Collection<Value> symTableValues,
                                                     Map<Integer, Value> heap) {
        Set<Integer> reachable = computeReachableAddresses(symTableValues, heap);

        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
