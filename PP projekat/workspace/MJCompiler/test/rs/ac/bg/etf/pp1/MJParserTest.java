package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.util.Log4JUtils;

import rs.ac.bg.etf.pp1.ast.Program;

public class MJParserTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		Logger log = Logger.getLogger(MJParserTest.class);
		
		Reader br = null;
		try {
			File sourceCode = new File("test/program.mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
			Symbol s = p.parse(); //Pocetak parsiranja
			
			Program prog = (Program)(s.value);
			//ispis sintaksnog stabla
			log.info(prog.toString());
			log.info("==================================");
			
			// ispis prepoznatih programskih instrukcija
			RuleVisitor v = new RuleVisitor();
			prog.traverseBottomUp(v);
			//log.info("Broj print poziva" + v.printNoSizeCallCount);
		}
		finally{
			if(br != null) try {
				br.close();
			}
			catch(IOException e1) {
				log.error(e1.getMessage(), e1);
			}
		}
		
	}

}
