package toy.model.exp;

import toy.exceptions.InterpreterException;
import toy.model.adt.Dictionary;
import toy.model.adt.Heap;
import toy.model.type.BoolType;
import toy.model.type.IntType;
import toy.model.type.Type;
import toy.model.value.BoolValue;
import toy.model.value.IntValue;
import toy.model.value.Value;

public class RelationalExp implements Exp {
    private final Exp left;
    private final Exp right;
    private final String operator; // "<", "<=", "==", "!=", ">", ">="

    public RelationalExp(String operator, Exp left, Exp right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public Type typeCheck(Dictionary<String, Type> typeEnv) throws InterpreterException {
        Type type1 = left.typeCheck(typeEnv);
        Type type2 = right.typeCheck(typeEnv);

        if (!type1.equals(new IntType())) {
            throw new InterpreterException("First operand is not an integer, got: " + type1);
        }

        if (!type2.equals(new IntType())) {
            throw new InterpreterException("Second operand is not an integer, got: " + type2);
        }

        return new BoolType();
    }

    @Override
    public Value eval(Dictionary<String, Value> symTable, Heap heap) throws InterpreterException {
        Value v1 = left.eval(symTable, heap);
        if (!v1.getType().equals(new IntType())) {
            throw new InterpreterException("First operand is not an int, got: " + v1.getType());
        }

        Value v2 = right.eval(symTable, heap);
        if (!v2.getType().equals(new IntType())) {
            throw new InterpreterException("Second operand is not an int, got: " + v2.getType());
        }

        int n1 = ((IntValue) v1).getValue();
        int n2 = ((IntValue) v2).getValue();

        boolean result = switch (operator) {
            case "<" -> n1 < n2;
            case "<=" -> n1 <= n2;
            case "==" -> n1 == n2;
            case "!=" -> n1 != n2;
            case ">" -> n1 > n2;
            case ">=" -> n1 >= n2;
            default -> throw new InterpreterException("Invalid relational operator ': " + operator + "'");
        };

        return new BoolValue(result);
    }

    @Override
    public String toString() {
        return left.toString() + " " + operator + " " + right.toString();
    }
}
