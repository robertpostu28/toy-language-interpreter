package toy.repository;

import toy.exceptions.InterpreterException;
import toy.model.statement.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class InMemoryRepository implements Repository {
    private PrgState program;
    private final Path logPath;

    public InMemoryRepository(String logFilePath) {
        this.logPath = Path.of(logFilePath);
        try {
            Files.deleteIfExists(logPath);
            Files.createFile(logPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize log file: " + e.getMessage());
        }
    }

    @Override
    public PrgState getProgram() {
        return program;
    }

    @Override
    public void setProgram(PrgState program) {
        this.program = program;
    }

    @Override
    public void logPrgStateExec() throws InterpreterException {
        if (program == null) {
            throw new InterpreterException("No program state set in repository.");
        }
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logPath.toFile(), true)))) {
            logFile.println(program.toString());
            logFile.println("-----------------------------------------");
        } catch (IOException e) {
            throw new InterpreterException("Error while logging program state: " + e.getMessage());
        }
    }
}
