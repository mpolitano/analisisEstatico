package main;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

import ast.Program;
import graphs.ASTtoCFGVisitor;
import graphs.CFG;

public class Main {
	private static parser parser;	// Parser
    private static FileReader programFile;

	static public void main(String[] argv) {
		try {
			String fileName = "graph.dot";
			FileWriter f = new FileWriter(fileName);
			parser p = new parser(new AnalizadorLexico( new FileReader("src/test/test")));
			Program result = (Program) p.parse().value;
			System.out.println("Parser successfully created");
			System.out.println("Translating AST to CFG");
			CFG cfg = new CFG();
			ASTtoCFGVisitor visitor = new ASTtoCFGVisitor();
			cfg = result.accept(visitor);
			cfg.toDot(fileName);
			System.out.println("CFG successfully created in TP/graph.dot ");
		} catch (Exception e) {
			System.out.println(e.getMessage());
            e.printStackTrace(System.out);

		}
	}

}
