package org.smile.orm.parser;

import org.smile.expression.Context;
import org.smile.expression.SimpleExpression;
import org.smile.expression.visitor.ExpressionVisitor;

public class InMultiParameterExpression extends SimpleExpression {
    String params;
    public InMultiParameterExpression(String text){
        this.params =text;
    }

    @Override
    public Object evaluate(Context root) {
        return null;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {

    }

    @Override
    public String toString() {
        return params;
    }
}
