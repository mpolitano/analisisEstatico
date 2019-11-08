/**
*
* Class AST is used for management the abstrac syntax tree.
*
* It is abstract because many of the class which inherit of him will implements more methods or use those who are already done.
*
*
*/
package ast;

import ast.ASTVisitor;


public abstract class AST {

	/**
	* Method needed by the visitor patron, it is abstrac because the class which inherit implements it
	*
	* @see ASTVisitor
	*/
	public abstract<T> T accept(ASTVisitor<T> v);
}