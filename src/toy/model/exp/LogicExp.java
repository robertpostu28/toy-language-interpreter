package toy.model.exp;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.type.BoolType;
import toy.model.value.BoolValue;
import toy.model.value.Value;
import toy.model.adt.Heap;
import toy.model.type.Type;

public class LogicExp implements Exp {
    public enum Op { AND, OR }

    private final Exp left;
    private final Exp right;
    private final Op operator;

    public LogicExp(Exp left, Exp right, Op operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public static LogicExp of(String opText, Exp left, Exp right) {
        return switch (opText.toLowerCase()) {
            case "and", "&&" -> new LogicExp(left, right, Op.AND);
            case "or", "||" -> new LogicExp(left, right, Op.OR);
            default -> throw new IllegalArgumentException("Unknown logical operator: " + opText);
        };
    }

    @Override
    public Type typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        Type t1 = left.typeCheck(typeEnv);
        Type t2 = right.typeCheck(typeEnv);

        if (!t1.equals(new BoolType())) {
            throw new InterpreterException("First operand is not a boolean! It is: " + t1);
        }
        if (!t2.equals(new BoolType())) {
            throw new InterpreterException("Second operand is not a boolean! It is: " + t2);
        }

        return new BoolType();
    }

    @Override
    public Value eval(Dictionary<String, Value> symTable, Heap heap) throws InterpreterException {
        Value v1 = left.eval(symTable, heap);
        if (!v1.getType().equals(new BoolType())) {
            throw new InterpreterException("First operand is not a boolean! It is: " + v1.getType());
        }
        Value v2 = right.eval(symTable, heap);
        if (!v2.getType().equals(new BoolType())) {
            throw new InterpreterException("Second operand is not a boolean! It is: " + v2.getType());
        }

        boolean b1 = ((BoolValue) v1).getValue();
        boolean b2 = ((BoolValue) v2).getValue();

        return switch (operator) {
            case AND -> new BoolValue(b1 && b2);
            case OR -> new BoolValue(b1 || b2);
        };
    }

    @Override
    public String toString() {
        String opStr = switch (operator) {
            case AND -> "&&";
            case OR -> "||";
        };
        return "(" + left.toString() + " " + opStr + " " + right.toString() + ")";
    }
}
