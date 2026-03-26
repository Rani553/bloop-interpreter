// ─── Expression Interface ───────────────────────────────────────────────────

public interface Expression {
    /**
     * Evaluate this expression using the current variable store.
     * Returns either a Double (for numbers) or a String (for text).
     */
    Object evaluate(Environment env);
}


// ─── AssignInstruction ───────────────────────────────────────────────────────
// Handles:  put <expr> into <variable>
// Example:  put x + y * 2 into result

class AssignInstruction implements Instruction {
    private final String variableName;
    private final Expression expression;

    public AssignInstruction(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression   = expression;
    }

    @Override
    public void execute(Environment env) {
        // Evaluate the expression, then store the result under the variable name.
        Object value = expression.evaluate(env);
        env.set(variableName, value);
    }
}



// ─── IfInstruction ───────────────────────────────────────────────────────────
// Handles:  if <condition> then:
//               <body instructions>
// Example:  if score > 50 then:
//               print "Pass"

class IfInstruction implements Instruction {
    private final Expression condition;
    private final List<Instruction> body;

    public IfInstruction(Expression condition, List<Instruction> body) {
        this.condition = condition;
        this.body      = body;
    }

    @Override
    public void execute(Environment env) {
        Object result = condition.evaluate(env);

        // The condition must evaluate to a Boolean (produced by BinaryOpNode comparisons).
        if (result instanceof Boolean && (Boolean) result) {
            for (Instruction instruction : body) {
                instruction.execute(env);
            }
        }
    }
}