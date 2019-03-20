package main;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.regex.Matcher;
import main.Helper;

public class Lexer {
	static ArrayList<Expression> regexRules = new ArrayList<Expression>();
	
	public static void initRules(){
		regexRules.add(new Expression("LineComment","(//(.*?)[\r$]?\n).*"));
		regexRules.add(new Expression("Email", "(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*"));
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		initRules();
	}

}
