package ast;
/**
*
* This class represents a Block node in the AST. It inherit from Statement.
*
* @autor cornejo-politano-raverta.
* 
*/
import java.util.List;
import ast.ASTVisitor;
import java.util.LinkedList;


public class Block extends Statement {
	
	/**
	* Class's atributes.
	*/
	private List<Statement> statements;
	

	
	/**
	* Constructor of a Block object.
	*
	* @param s - list which contains the differents statements of the block.
	*
	*/
	public Block(List<Statement> s) {		
		statements = s;
	}

	/**
	*
	* Methods set and get.
	*
	*/	

	/** Method to get the block's statements list. */
	public List<Statement> getStatements() {
		return this.statements;
	} 

	/**
	* Method to set the block's statements.
	*
	* @param statements - A list to be seted in the statements atribute of the object.
	*
	*/
	public void setStatements(List<Statement> statements){
		this.statements=statements;
	}
	

	/**
	* New implementation of the method toString().
	*
	* @see String#toString()
	*/
	@Override
	public String toString() {
		String rtn = "";
		
	    for (Statement s: statements) {
			rtn += s.toString() + '\n';
		}
		
		if (rtn.length() > 0) return rtn.substring(0, rtn.length() - 1); // remove last new line char
		
		return rtn; 
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