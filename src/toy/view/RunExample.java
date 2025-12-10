package toy.view;

import toy.controller.Controller;
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
        try {
            // run the whole program (this also logs after each step)
            controller.allStep();

            // get final program state from repository and print OUT
            PrgState prg = repo.getProgram();
            if (prg != null) {
                System.out.println("Program output: " + prg.getOut());
            } else {
                System.out.println("Program finished, but repository has no program state.");
            }
        } catch (InterpreterException e) {
            // on error, log last state if possible
            try {
                repo.logPrgStateExec();
            } catch (InterpreterException ignored) { }

            System.out.println("Interpreter error: " + e.getMessage());
        }
        System.out.println();
    }
}
