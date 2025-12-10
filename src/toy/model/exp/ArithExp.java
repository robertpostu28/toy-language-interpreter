package toy.model.exp;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.adt.Heap;
import toy.model.value.Value;
import toy.model.value.IntValue;
import toy.model.type.IntType;

public class ArithExp implements Exp {
    private final Exp left;
    private final Exp right;
    private final char operation; // '+', '-', '*', '/'

    public ArithExp(Exp left, Exp right, char operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public Value eval(Dictionary<String, Value> symTable, Heap heap) throws InterpreterException {
        Value v1 = left.eval(symTable, heap);
        if (!v1.getType().equals(new IntType())) {
            throw new InterpreterException("First operand is not an integer! It is: " + v1.getType());
        }
        Value v2 = right.eval(symTable, heap);
        if (!v2.getType().equals(new IntType())) {
            throw new InterpreterException("Second operand is not an integer! It is: " + v2.getType());
        }

        int n1 = ((IntValue) v1).getValue();
        int n2 = ((IntValue) v2).getValue();

        return switch (operation) {
            case '+' -> new IntValue(n1 + n2);
            case '-' -> new IntValue(n1 - n2);
            case '*' -> new IntValue(n1 * n2);
            case '/' -> {
                if (n2 == 0) {
                    throw new InterpreterException("Division by zero!");
                }
                yield new IntValue(n1 / n2);
            }
            default -> throw new InterpreterException("Invalid operation: " + operation);
        };
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + operation + " " + right.toString() + ")";
    }
}
