package main;

import java.io.FileInputStream;
import java.io.FileReader;

import ast.Program;
import graphs.ASTtoCFGVisitor;
import graphs.CFG;

public class Main {
	private static parser parser;	// Parser
    private static FileReader programFile;

	static public void main(String[] argv) {
		try {
        //    programFile = new FileReader(argv[0]);
       //     parser = new parser(new AnalizadorLexico(programFile));
  //         Program program = (Program)parser.parse().value;
           
          parser p = new parser(new AnalizadorLexico( new FileReader("src/test/test")));
			Program result = (Program) p.parse().value;
			System.out.println("a");
			CFG cfg = new CFG();
			ASTtoCFGVisitor visitor = new ASTtoCFGVisitor();
			cfg = result.accept(visitor);
		} catch (Exception e) {
			System.out.println(e.getMessage());
            e.printStackTrace(System.out);

		}
	}

}
