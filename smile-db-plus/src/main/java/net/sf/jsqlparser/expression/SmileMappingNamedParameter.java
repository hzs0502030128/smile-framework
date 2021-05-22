package net.sf.jsqlparser.expression;

public class SmileMappingNamedParameter extends JdbcNamedParameter{
	
	  public void accept(ExpressionVisitor expressionVisitor)
	  {
	    expressionVisitor.visit(this);
	  }

	  public String toString()
	  {
	    return "%{"+getName()+"}";
	  }
}
