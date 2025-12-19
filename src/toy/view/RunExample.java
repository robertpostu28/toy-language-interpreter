package toy.view;

import toy.controller.Controller;
import toy.controller.InterpreterController;
import toy.exceptions.InterpreterException;
import toy.model.statement.PrgState;
import toy.repository.Repository;

public class RunExample extends Command {
    private final Controller controller;
    private final Repository repo;

    public RunExample(String key, String description,
                      Controller controller, Repository repo) {
        super(key, description);
        this.controller = controller;
        this.repo = repo;
    }

    @Override
    public void execute() {
        System.out.println("\nRunning example: " + getDescription());

        // keep a reference to the initial program state (so we can read Out at the end)
        PrgState initial = null;
        var listBefore = repo.getProgramsList();
        if (!listBefore.isEmpty()) {
            initial = listBefore.get(0);
        }

        try {
            controller.typeCheckAll();
            controller.allStep();

            if (initial != null) {
                System.out.println("Program output: " + initial.getOut());
            } else {
                System.out.println("No program to run.");
            }
        } catch (InterpreterException e) {
            try {
                for (PrgState p : repo.getProgramsList()) {
                    repo.logPrgStateExec(p);
                }
            } catch (InterpreterException ignored) { }

            System.out.println("Interpreter error: " + e.getMessage());
        }

        System.out.println();
    }
}
