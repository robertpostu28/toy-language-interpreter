package toy.repository;

import toy.exceptions.InterpreterException;
import toy.model.statement.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository implements Repository {
    private List<PrgState> programsList;
    private final Path logPath;

    public InMemoryRepository(String logFilePath) {
        Path logsDir = Path.of("logs");

        try {
            Files.createDirectories(logsDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create logs directory: " + e.getMessage());
        }

        this.logPath = logsDir.resolve(logFilePath);
        this.programsList = new ArrayList<>();

        try {
            Files.deleteIfExists(this.logPath);
            Files.createFile(this.logPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize log file: " + e.getMessage());
        }
    }

    @Override
    public List<PrgState> getProgramsList() {
        return programsList;
    }

    @Override
    public void setProgramsList(List<PrgState> programs) {
        this.programsList = programs;
    }

    @Override
    public void logPrgStateExec(PrgState program) throws InterpreterException {
        try (var logFile = new PrintWriter(new FileWriter(logPath.toFile(), true))) {
            logFile.println(program.toString());
            logFile.println("--------------------------------------------------");
        } catch (IOException exception) {
            throw new InterpreterException("Could not log program state: " + exception.getMessage());
        }
    }
}
