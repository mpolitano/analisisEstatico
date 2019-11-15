/**
*
* Enumeration class which represents the diferents and possible type of a binary operation.
*
* @autor cornejo-politano-raverta.
*
*/
package ast;


public enum BinOpType {

	/**
	*
	* The values of the class enumeration.
	*
	*/
	PLUS{
		public String toString(){
			return "+";
		}
	} // Arithmetic


//	/**
//	* New implementation of the method toString.
//	*
//	* @see String#toString().
//	*/
//	@Override
//	public String toString() {
//		switch(this) {
//			case PLUS:
//				return "+";
//		}
//		
//		return null;
//	}
	
	
	
}