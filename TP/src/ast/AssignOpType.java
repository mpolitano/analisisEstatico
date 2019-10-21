/**
*
* Enumeration class which represents the diferents and possible type of assignation.
*
* @autor cornejo-politano.
*
*/


package ast;

public enum AssignOpType {
	
	/**
	* The values of the class enumeration.
	*/
	ASSIGN;
	
	/**
	* New implementation of the method toString.
	*
	* @see String#toString().
	*/
	@Override
	public String toString() {
		switch(this) {
			case ASSIGN:
				return "=";
		}
		
		return null;		
	}
}