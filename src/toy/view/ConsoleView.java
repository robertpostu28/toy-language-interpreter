package toy.view;

import toy.controller.Controller;
import toy.controller.InterpreterController;
import toy.exceptions.InterpreterException;
import toy.model.adt.RuntimeDictionary;
import toy.model.adt.RuntimeSequence;
import toy.model.adt.RuntimeStack;
import toy.model.exp.ArithExp;
import toy.model.exp.LogicExp;
import toy.model.exp.RelationalExp;
import toy.model.exp.ValueExp;
import toy.model.exp.VarExp;
import toy.model.statement.*;
import toy.model.type.BoolType;
import toy.model.type.IntType;
import toy.model.type.RefType;
import toy.model.type.StringType;
import toy.model.value.BoolValue;
import toy.model.value.IntValue;
import toy.model.value.StringValue;
import toy.model.value.Value;
import toy.repository.InMemoryRepository;
import toy.repository.Repository;
import toy.model.statement.WhileStatement;

import toy.model.exp.ReadHeapExp;
import toy.model.statement.NewStatement;
import toy.model.statement.WriteHeapStatement;


import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class ConsoleView implements View {

    public static void main(String[] args) throws InterpreterException {
        View view = new ConsoleView();
        view.run();
    }

    @Override
    public void run() {
        // EX1: int v; v=2; print(v);   (prints 2)
        Statement ex1 =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ValueExp(new IntValue(2))),
                                new PrintStatement(new VarExp("v"))
                        )
                );

        // EX2: int a; int b; a=2+3*5; b=a-4/2+7; print(b);  (prints 22)
        Statement ex2 =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new IntType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("b", new IntType()),
                                new CompoundStatement(
                                        new AssignmentStatement(
                                                "a",
                                                new ArithExp(
                                                        new ValueExp(new IntValue(2)),
                                                        new ArithExp(
                                                                new ValueExp(new IntValue(3)),
                                                                new ValueExp(new IntValue(5)),
                                                                '*'
                                                        ),
                                                        '+'
                                                )
                                        ),
                                        new CompoundStatement(
                                                new AssignmentStatement(
                                                        "b",
                                                        new ArithExp(
                                                                new ArithExp(
                                                                        new VarExp("a"),
                                                                        new ArithExp(
                                                                                new ValueExp(new IntValue(4)),
                                                                                new ValueExp(new IntValue(2)),
                                                                                '/'
                                                                        ),
                                                                        '-'
                                                                ),
                                                                new ValueExp(new IntValue(7)),
                                                                '+'
                                                        )
                                                ),
                                                new PrintStatement(new VarExp("b"))
                                        )
                                )
                        )
                );

        // EX3: bool a; a=false; int v; if(a) then v=2 else v=3; print(v);   (prints 3)
        Statement ex3 =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new BoolType()),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ValueExp(new BoolValue(false))),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("v", new IntType()),
                                        new CompoundStatement(
                                                new IfStatement(
                                                        new VarExp("a"),
                                                        new AssignmentStatement("v", new ValueExp(new IntValue(2))),
                                                        new AssignmentStatement("v", new ValueExp(new IntValue(3)))
                                                ),
                                                new PrintStatement(new VarExp("v"))
                                        )
                                )
                        )
                );

        // EX4: logical expressions
        // bool a; bool b;
        // a = true; b = false;
        // print(a && b); print(a || b);   (prints false then true)
        Statement ex4 =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new BoolType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("b", new BoolType()),
                                new CompoundStatement(
                                        new AssignmentStatement("a", new ValueExp(new BoolValue(true))),
                                        new CompoundStatement(
                                                new AssignmentStatement("b", new ValueExp(new BoolValue(false))),
                                                new CompoundStatement(
                                                        new PrintStatement(
                                                                LogicExp.of("and", new VarExp("a"), new VarExp("b"))
                                                        ),
                                                        new PrintStatement(
                                                                LogicExp.of("or", new VarExp("a"), new VarExp("b"))
                                                        )
                                                )
                                        )
                                )
                        )
                );

        // EX5: relational expressions
        // int a; int b;
        // a = 5; b = 10;
        // print(a < b);
        // print(a >= 5);
        // print(a == b);
        // print(a != b);
        // (prints: true, true, false, true)
        Statement ex5 =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new IntType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("b", new IntType()),
                                new CompoundStatement(
                                        new AssignmentStatement("a", new ValueExp(new IntValue(5))),
                                        new CompoundStatement(
                                                new AssignmentStatement("b", new ValueExp(new IntValue(10))),
                                                new CompoundStatement(
                                                        new PrintStatement(
                                                                new RelationalExp("<", new VarExp("a"), new VarExp("b"))
                                                        ),
                                                        new CompoundStatement(
                                                                new PrintStatement(
                                                                        new RelationalExp(">=", new VarExp("a"),
                                                                                new ValueExp(new IntValue(5)))
                                                                ),
                                                                new CompoundStatement(
                                                                        new PrintStatement(
                                                                                new RelationalExp("==", new VarExp("a"), new VarExp("b"))
                                                                        ),
                                                                        new PrintStatement(
                                                                                new RelationalExp("!=", new VarExp("a"), new VarExp("b"))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        // EX6: file, single read
        // string fname; fname="test.in"; openRFile(fname);
        // int x; readFile(fname,x); print(x); closeRFile(fname);
        // (prints the first int from test.in)
        Statement ex6 =
                new CompoundStatement(
                        new VariableDeclarationStatement("fname", new StringType()),
                        new CompoundStatement(
                                new AssignmentStatement("fname",
                                        new ValueExp(new StringValue("test.in"))),
                                new CompoundStatement(
                                        new OpenRFileStatement(new VarExp("fname")),
                                        new CompoundStatement(
                                                new VariableDeclarationStatement("x", new IntType()),
                                                new CompoundStatement(
                                                        new ReadFileStatement(new VarExp("fname"), "x"),
                                                        new CompoundStatement(
                                                                new PrintStatement(new VarExp("x")),
                                                                new CloseRFileStatement(new VarExp("fname"))
                                                        )
                                                )
                                        )
                                )
                        )
                );

        // EX7: file, multiple reads
        // string fname; fname="test.in"; openRFile(fname);
        // int x;
        // readFile(fname,x); print(x);
        // readFile(fname,x); print(x);
        // readFile(fname,x); print(x);
        // readFile(fname,x); print(x);   // EOF -> 0
        // closeRFile(fname);
        // (prints first 3 ints from test.in, then 0)
        Statement ex7 =
                new CompoundStatement(
                        new VariableDeclarationStatement("fname", new StringType()),
                        new CompoundStatement(
                                new AssignmentStatement("fname",
                                        new ValueExp(new StringValue("test.in"))),
                                new CompoundStatement(
                                        new OpenRFileStatement(new VarExp("fname")),
                                        new CompoundStatement(
                                                new VariableDeclarationStatement("x", new IntType()),
                                                new CompoundStatement(
                                                        new ReadFileStatement(new VarExp("fname"), "x"),
                                                        new CompoundStatement(
                                                                new PrintStatement(new VarExp("x")),
                                                                new CompoundStatement(
                                                                        new ReadFileStatement(new VarExp("fname"), "x"),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VarExp("x")),
                                                                                new CompoundStatement(
                                                                                        new ReadFileStatement(new VarExp("fname"), "x"),
                                                                                        new CompoundStatement(
                                                                                                new PrintStatement(new VarExp("x")),
                                                                                                new CompoundStatement(
                                                                                                        new ReadFileStatement(new VarExp("fname"), "x"),
                                                                                                        new CompoundStatement(
                                                                                                                new PrintStatement(new VarExp("x")),
                                                                                                                new CloseRFileStatement(new VarExp("fname"))
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        // NEG1: x = 1;  (error: x is not declared)
        Statement neg1 =
                new AssignmentStatement("x", new ValueExp(new IntValue(1)));

        // NEG2: bool b; b = 1;  (error: type mismatch int vs bool)
        Statement neg2 =
                new CompoundStatement(
                        new VariableDeclarationStatement("b", new BoolType()),
                        new AssignmentStatement("b", new ValueExp(new IntValue(1)))
                );

        // NEG3: int v; v = 5 / 0; print(v);  (error: division by zero)
        Statement neg3 =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(
                                new AssignmentStatement(
                                        "v",
                                        new ArithExp(
                                                new ValueExp(new IntValue(5)),
                                                new ValueExp(new IntValue(0)),
                                                '/'
                                        )
                                ),
                                new PrintStatement(new VarExp("v"))
                        )
                );

        // NEG4: relational type mismatch
        // int a; bool flag; a=5; flag=true; print(flag < a);
        // (error: first operand is not int)
        Statement neg4 =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new IntType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("flag", new BoolType()),
                                new CompoundStatement(
                                        new AssignmentStatement("a", new ValueExp(new IntValue(5))),
                                        new CompoundStatement(
                                                new AssignmentStatement("flag", new ValueExp(new BoolValue(true))),
                                                new PrintStatement(
                                                        new RelationalExp("<", new VarExp("flag"), new VarExp("a"))
                                                )
                                        )
                                )
                        )
                );

        // EXNop: int v; v=10; nop; nop; print(v);   (prints 10)
        // Testing NopStatement
        Statement exNop =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ValueExp(new IntValue(10))),
                                new CompoundStatement(
                                        new NoOpStatement(),   // first nop
                                        new CompoundStatement(
                                                new NoOpStatement(),   // second nop
                                                new PrintStatement(new VarExp("v"))
                                        )
                                )
                        )
                );

        // EXWhile: int v; v=4; while(v>0) { print(v); v=v-1; } print(v);
        Statement exWhile =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(
                                new AssignmentStatement("v",
                                        new ValueExp(new IntValue(4))),
                                new CompoundStatement(
                                        new WhileStatement(
                                                new RelationalExp(
                                                        ">",
                                                        new VarExp("v"),
                                                        new ValueExp(new IntValue(0))
                                                ),
                                                new CompoundStatement(
                                                        new PrintStatement(new VarExp("v")),
                                                        new AssignmentStatement(
                                                                "v",
                                                                new ArithExp(
                                                                        new VarExp("v"),
                                                                        new ValueExp(new IntValue(1)),
                                                                        '-'
                                                                )
                                                        )
                                                )
                                        ),
                                        new PrintStatement(new VarExp("v"))
                                )
                        )
                );

        // EX13: Ref int v; new(v,20); print(rH(v));  (Heap: {1->20}, Out: [20])
        Statement exRef1 =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new RefType(new IntType())),
                        new CompoundStatement(
                                new NewStatement("v",
                                        new ValueExp(new IntValue(20))),
                                new PrintStatement(
                                        new ReadHeapExp(new VarExp("v"))
                                )
                        )
                );

        // EX14: Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5);  (Heap: {1->30}, Out: [20,35])
        Statement exRef2 =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new RefType(new IntType())),
                        new CompoundStatement(
                                new NewStatement("v",
                                        new ValueExp(new IntValue(20))),
                                new CompoundStatement(
                                        new PrintStatement(
                                                new ReadHeapExp(new VarExp("v"))
                                        ),
                                        new CompoundStatement(
                                                new WriteHeapStatement(
                                                        "v",
                                                        new ValueExp(new IntValue(30))
                                                ),
                                                new PrintStatement(
                                                        new ArithExp(
                                                                new ReadHeapExp(new VarExp("v")),
                                                                new ValueExp(new IntValue(5)),
                                                                '+'
                                                        )
                                                )
                                        )
                                )
                        )
                );

        // EX16: Ref Ref int v; new(v,20); new(a,v); new(v,30); print(rH(rH(a)));  (prints 20)
        Statement exGC =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new RefType(new IntType())),
                        new CompoundStatement(
                                new NewStatement("v", new ValueExp(new IntValue(20))),
                                new CompoundStatement(
                                        new VariableDeclarationStatement(
                                                "a",
                                                new RefType(new RefType(new IntType()))
                                        ),
                                        new CompoundStatement(
                                                new NewStatement("a", new VarExp("v")),
                                                new CompoundStatement(
                                                        new NewStatement("v", new ValueExp(new IntValue(30))),
                                                        new PrintStatement(
                                                                new ReadHeapExp(
                                                                        new ReadHeapExp(new VarExp("a"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        // EXFork: int v; v=10; Ref int a; new(a,22);
        // fork( wH(a,30); v=32; print(v); print(rH(a)) );
        // print(v); print(rH(a));
        Statement exFork =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("a", new RefType(new IntType())),
                                new CompoundStatement(
                                        new AssignmentStatement("v", new ValueExp(new IntValue(10))),
                                        new CompoundStatement(
                                                new NewStatement("a", new ValueExp(new IntValue(22))),
                                                new CompoundStatement(
                                                        new ForkStatement(
                                                                new CompoundStatement(
                                                                        new WriteHeapStatement("a", new ValueExp(new IntValue(30))),
                                                                        new CompoundStatement(
                                                                                new AssignmentStatement("v", new ValueExp(new IntValue(32))),
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(new VarExp("v")),
                                                                                        new PrintStatement(new ReadHeapExp(new VarExp("a")))
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        new CompoundStatement(
                                                                new PrintStatement(new VarExp("v")),
                                                                new PrintStatement(new ReadHeapExp(new VarExp("a")))
                                                        )
                                                )
                                        )
                                )
                        )
                );



        RunExample cmd1 = buildCommandForProgram(
                "1",
                "int v; v=2; print(v); (prints 2)",
                ex1,
                "log1.txt"
        );

        RunExample cmd2 = buildCommandForProgram(
                "2",
                "int a; int b; a=2+3*5; b=a-4/2+7; print(b); (prints 22)",
                ex2,
                "log2.txt"
        );

        RunExample cmd3 = buildCommandForProgram(
                "3",
                "bool a; a=false; int v; if(a) then v=2 else v=3; print(v); (prints 3)",
                ex3,
                "log3.txt"
        );

        RunExample cmd4 = buildCommandForProgram(
                "4",
                "bool a; bool b; a=true; b=false; print(a && b); print(a || b); (prints false, true)",
                ex4,
                "log4_logic.txt"
        );

        RunExample cmd5 = buildCommandForProgram(
                "5",
                "int a; int b; a=5; b=10; print(a<b); print(a>=5); print(a==b); print(a!=b); (prints true, true, false, true)",
                ex5,
                "log5_rel.txt"
        );

        RunExample cmd6 = buildCommandForProgram(
                "6",
                 "string fname; fname=\"test.in\"; openRFile(fname); int x; readFile(fname,x); print(x); closeRFile(fname); (prints first int from test.in)",
                ex6,
                "log6_file1.txt"
        );

        RunExample cmd7 = buildCommandForProgram(
                "7",
                "file test (multiple reads) – prints first 3 ints then 0 from test.in",
                ex7,
                "log7_file2.txt"
        );

        RunExample cmdNeg1 = buildCommandForProgram(
                "8",
                "x = 1; (ERROR: variable x is not declared)",
                neg1,
                "log_neg1_undeclared.txt"
        );

        RunExample cmdNeg2 = buildCommandForProgram(
                "9",
                "bool b; b = 1; (ERROR: type mismatch int -> bool)",
                neg2,
                "log_neg2_typemismatch.txt"
        );

        RunExample cmdNeg3 = buildCommandForProgram(
                "10",
                "int v; v = 5 / 0; print(v); (ERROR: division by zero)",
                neg3,
                "log_neg3_div0.txt"
        );

        RunExample cmdNeg4 = buildCommandForProgram(
                "11",
                "int a; bool flag; a=5; flag=true; print(flag < a); (ERROR: first operand of < is not int)",
                neg4,
                "log_neg4_reltype.txt"
        );

        RunExample cmd12 = buildCommandForProgram(
                "12",
                "int v; v=10; nop; nop; print(v); (prints 10)",
                exNop,
                "log12_nop.txt"
        );

        RunExample cmd13 = buildCommandForProgram(
                "13",
                "int v; v=4; while(v>0) { print(v); v=v-1; } print(v);",
                exWhile,
                "log13_while.txt"
        );

        RunExample cmd14 = buildCommandForProgram(
                "14",
                "Ref int v; new(v,20); print(rH(v)); (prints 20)",
                exRef1,
                "log14_ref1.txt"
        );

        RunExample cmd15 = buildCommandForProgram(
                "15",
                "Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5); (prints 20 and 35)",
                exRef2,
                "log15_ref2.txt"
        );

        RunExample cmd16 = buildCommandForProgram(
                "16",
                "Ref Ref int v; new(v,20); new(a,v); new(v,30); print(rH(rH(a))); (prints 20)",
                exGC,
                "log16_gc.txt"
        );

        RunExample cmd17 = buildCommandForProgram(
                "17",
                "int v; v=10; Ref int a; new(a,22); fork( wH(a,30); v=32; print(v); print(rH(a)) ); print(v); print(rH(a));",
                exFork,
                "log17_fork.txt"
        );


        // ======================
        // 3. BUILD & RUN MENU
        // ======================

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(cmd1);
        menu.addCommand(cmd2);
        menu.addCommand(cmd3);
        menu.addCommand(cmd4);
        menu.addCommand(cmd5);
        menu.addCommand(cmd6);
        menu.addCommand(cmd7);
        menu.addCommand(cmdNeg1);
        menu.addCommand(cmdNeg2);
        menu.addCommand(cmdNeg3);
        menu.addCommand(cmdNeg4);
        menu.addCommand(cmd12);
        menu.addCommand(cmd13);
        menu.addCommand(cmd14);
        menu.addCommand(cmd15);
        menu.addCommand(cmd16);
        menu.addCommand(cmd17);

        menu.show();  // prints menu + executes selected command in a loop
    }

    /**
     * Helper: builds PrgState, Repository, Controller and wraps them into a RunExample command.
     */
    private RunExample buildCommandForProgram(String key,
                                              String description,
                                              Statement program,
                                              String logFile) {

        var stack = new RuntimeStack<Statement>();
        var symTable = new RuntimeDictionary<String, Value>();
        var out = new RuntimeSequence<Value>();
        var fileTable = new RuntimeDictionary<StringValue, BufferedReader>();
        var heap = new toy.model.adt.RuntimeHeap();

        PrgState state = new PrgState(stack, symTable, out, fileTable, heap, program);

        Repository repo = new InMemoryRepository(logFile);
        repo.setProgramsList(new ArrayList<>(List.of(state)));

        Controller controller = new InterpreterController(repo);

        return new RunExample(key, description, controller, repo);
    }
}
