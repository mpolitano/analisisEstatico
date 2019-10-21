package ast;
public class BinOpExpr extends Expression {

	/**
	* Class's atributes.
	*/
	private BinOpType operator; //operator in the expr = expr operator expr
	private Expression lOperand; //left expression
	private Expression rOperand; //right expression
	

	/**
	* Constructor of an BinOp object.
	*
	* @param l - left operand.
	* @param op - type of the binary operation.
	* @param r - right operand.
	* @param my_line/my_column - to report an error. 
	*
	*/
	public BinOpExpr(Expression l, BinOpType op, Expression r ){
		operator = op;
		lOperand = l;
		rOperand = r;
	}


	/**
	*
	* Methods set and get.
	*
	*/

	/**
	* Method to get the operator of the binary operation. 
	*
	* @return the operator
	*/
	public BinOpType getOperator() {
		return operator;
	}

	/**
	* Method to set the operator of the binary operation. 
	*
	* @param operator - The operator to be seted.
	*/
	public void setOperator(BinOpType operator) {
		this.operator = operator;
	}

	/**
	* Method to get the Left Operand of the binary operation. 
	*
	* @return the left operand
	*/
	public Expression getLeftOperand() {
		return lOperand;
	}

	/**
	* Method to set the left operand of the binary operation. 
	*
	* @param lOperand - The operand to be seted.
	*/
	public void setLeftOperand(Expression lOperand) {
		this.lOperand = lOperand;
	}

	/**
	* Method to get the Right Operand of the binary operation. 
	*
	* @return the right operand
	*/
	public Expression getRightOperand() {
		return rOperand;
	}
	
	/**
	* Method to set the right operand of the binary operation. 
	*
	* @param rOperand - The operand to be seted.
	*/
	public void setRightOperand(Expression rOperand) {
		this.rOperand = rOperand;
	}
	
	/**
	* New implementation of the method toString.
	*
	* @see String#toString().
	*/
	@Override
	public String toString() {
		return lOperand + " " + operator + " " + rOperand;
	}


	@Override
	public <T> T accept(ASTVisitor<T> v) {
		// TODO Auto-generated method stub
		return null;
	}

}