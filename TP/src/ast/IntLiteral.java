/**
*
* This class represents a IntLiteral node in the AST. It inherit from Literal<Integer>.
* 
* @autor cornejo-politano-raverta.
*
*/
package ast;


public class IntLiteral extends Expression {
	/**
	* Class's atributes.
	*/
	private Integer value;
	
	/**
	 * Constructor of a IntLiteral object.
	 * 
	 * @param: val - the IntLiteral's value in String.
	 * @param my_line/my_col - to report an error.
	 */
	public IntLiteral(Integer val){
		value = val;//Parses the string argument as a signed decimal integer.

	}

	/**
	 * Constructor of a IntLiteral object.
	 * 
	 * @param: val - the IntLiteral's value in int.
	 */
	public IntLiteral(int val, int my_line,int my_column){
		value = new Integer(val);//Parses the string argument as a signed decimal integer.

	}

	/**
	*
	* Methods set and get
	*
	*/

	/**
	* New implementation of the method getType().
	*
	* @return IntLiteral's Type.
	*
	* @see Literal<TValue>#getType()
	*/
	@Override
	public Type getType() {
		return Type.INT;
	}


	/**
	* Method to get the IntLiteral's value like a Integer. 
	*
	* @return the IntLiteral's value in Integer.
	*/
	public Integer getValue() {
		return value;
	}

	/**
	* Method to set the IntLiteral's int value. 
	*
	* @param The int to be seted.
	*/
	public void setValue(int value) {
		this.value = value;
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