/**
*
* Class Statement represent a Statement node in the ast. Inherit from AST.
*
* It is abstract because the classes which inherit of him implements it.
*
* @autor cornejo-politano-raverta.
*
*/
package ast;

public abstract class Statement extends AST {
	
	public abstract String toString();
}