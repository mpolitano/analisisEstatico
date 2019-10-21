package ast;
public class AssignStmt extends Statement {
	/**
	* Class's atributes.
	*/
	private Expression expr;
	private String id;


	/**
	* Constructor of an AssignStmt object.
	*
	* @param id - id
	* @param e - the expression to be assigned in loc.
	*
	*/
	public AssignStmt(String id, Expression e) {
		this.expr = e;
		this.id = id;
	
	}

	/**
	*
	* Methods set and get
	*
	*/
	
	/**
	* Method to set the assignStmt's expression. 
	*
	* @param e - The expression to be seted.
	*/
	public void setExpression(Expression e) {
		this.expr = e;
	}
	
	/** Method to get the assignStmt's expression. */
	public Expression getExpression() {
		return this.expr;
	}
	
	/** Method to get the assignStmt's operator. */
	public String getID() {
		return this.id;
	}

	/**
	* Method to set the assignStmt's operator. 
	*
	* @param operator - The operator to be seted.
	*/
	public void setID(String id) {
		this.id = id;
	}
	
	/**
	* New implementation of the method toString.
	*
	* @see String#toString().
	*/
	@Override
	public String toString() {
		return id + " = " +  expr.toString();
		
	}

	/**
	* New implementation of the method accept.
	*
	* @see AST#accept(ASTVisitor<T> v)
	*/
	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}

}