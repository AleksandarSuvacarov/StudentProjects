package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.PrintNoSizeStmt;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class RuleVisitor extends VisitorAdaptor{
	
	Logger log = Logger.getLogger(getClass());
	int printNoSizeCall = 0;
	
	public void visit(PrintNoSizeStmt PrintNoSizeStmt) {
		printNoSizeCall++;
		//log.info("Prepoznata naredba printNoSize");
	}
	
}
