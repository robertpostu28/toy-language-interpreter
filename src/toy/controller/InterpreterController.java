package toy.controller;

import toy.exceptions.InterpreterException;
import toy.model.adt.RuntimeDictionary;
import toy.model.adt.Stack;
import toy.model.statement.PrgState;
import toy.model.statement.Statement;
import toy.model.value.RefValue;
import toy.model.value.Value;
import toy.repository.Repository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class InterpreterController implements Controller {
    private final Repository repo;
    private ExecutorService executor;

    public InterpreterController(Repository repo) {

        this.repo = repo;
        this.executor = null;
    }

    @Override
    public PrgState oneStep(PrgState state) throws InterpreterException {
        return state.oneStep();
    }

    private List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void oneStepForAllPrg(List<PrgState> prgList) throws InterpreterException {
        // execute one step for each program (concurrently)
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) p::oneStep)
                .toList();

        List<Future<PrgState>> futures;
        try {
            futures = executor.invokeAll(callList);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterpreterException("Execution interrupted: " + e.getMessage());
        }

        List<PrgState> newPrgList = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new RuntimeException("Error while executing one step: " + e);
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        // add the new programs to the list
        prgList.addAll(newPrgList);

        // log after
        prgList.forEach(prgState -> {
            try {
                repo.logPrgStateExec(prgState);
            } catch (InterpreterException e) {
                throw new RuntimeException(e);
            }
        });

        // save the current programs in the repository
        repo.setProgramsList(prgList);
    }

    @Override
    public void allStep() throws InterpreterException {
        executor = Executors.newFixedThreadPool(2);

        List<PrgState> prgList = removeCompletedPrg(repo.getProgramsList());

        for (PrgState prg : prgList) {
            repo.logPrgStateExec(prg);
        }

        while (!prgList.isEmpty()) {
            // GC: root addresses from all symbol tables

            List<Value> allSymTableValues = prgList.stream().
                    map(prg -> prg.getSymTable().asMap().values())
                    .flatMap(v -> v.stream())
                    .toList();

            // we presume that all programs share the same heap: we take the heap from the first program
            Map<Integer, Value> heapContent = prgList.get(0).getHeap().getContent();
            Map<Integer, Value> newHeap = safeGarbageCollector(allSymTableValues, heapContent);

            // update the heap of each program (same object, but safe)
            prgList.forEach(prg -> prg.getHeap().setContent(newHeap));

            oneStepForAllPrg(prgList);

            prgList = removeCompletedPrg(repo.getProgramsList());
        }

        executor.shutdownNow();
        repo.setProgramsList(prgList);
    }

    @Override
    public void typeCheckAll() throws InterpreterException {
        List<PrgState> prgList = repo.getProgramsList();
        if (prgList == null || prgList.isEmpty()) {
            throw new InterpreterException("No program states available for type checking.");
        }

        for (PrgState prg : prgList) {
            prg.getOriginalProgram().typeCheck(new RuntimeDictionary<>());
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
