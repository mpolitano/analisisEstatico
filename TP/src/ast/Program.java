/**
*
* Class Program represent a Program node in the ast. Inherit from AST.
*
* @autor cornejo-politano-raverta.
*
*/
package ast;

import java.util.*;

public class Program extends AST{
	/**
	* Class's atributes.
	*/
	private	Type type;
	private List<Statement> methods;
	private String id;

	/**
	 * Constructor of a Program object.
	 * 
	 * @param: type - Program's type.
	 * @param: id - id of Programs
	 * @param: my_methods - list of Program's methods
	 */
	public Program(Type type,String id, List<Statement> my_methods){
		type= type;
		methods= my_methods;
		id=id;
	}

	/**
	*
	* Methods set and get
	*
	*/

	/**
	* Method to get the Program's methods. 
	*
	* @return the list of methods.
	*/
	public List<Statement> getMethods(){
		return methods;
	}

	/**
	* Method to get the Program's locations. 
	*
	* @return the list of locations.
	*/
//	public List<Location> getFields(){
//		return fields;
//	}

	/**
	* Method to get the Program's id. 
	*
	* @return the Program's id.
	*/
	public String getId(){
		return id;
	}

	public String toString(){
		return "Fields: " + id.toString() + "\n"+ "Methods: " + methods.toString();
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