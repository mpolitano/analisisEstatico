package ast;
public class Variable extends Expression{

	String label;


	public Variable(String id){
		this.label = id;
	}

	@Override
	public String toString(){
		return label;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}