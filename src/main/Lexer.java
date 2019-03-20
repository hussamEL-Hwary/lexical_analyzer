package main;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.regex.Matcher;
import main.Helper;

public class Lexer {
	static ArrayList<Expression> regexRules = new ArrayList<Expression>();
	/**
	 * initialize regexRules with regular expressions
	 */
	public static void initRules(){
		regexRules.add(new Expression("LineComment","(//(.*?)[\r$]?\n)"));
		regexRules.add(new Expression("Email", "(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*"));
	}

	
	/**
	 * get tokens
	 * 
	 * @param content
	 * @return
	 */
	public static ArrayList<Token> get_tokens(String content){
		ArrayList<Token> tokens = new ArrayList<Token>();
		while(!content.isEmpty()) {
			boolean isfound = false;
			for(Expression ex: regexRules) {
				Pattern pattern = Pattern.compile(ex.expRule);
				Matcher matcher = pattern.matcher(content);
				if(matcher.find(0) && matcher.start()==0) {
					Token token_found = new Token(ex.type, matcher.group());
					tokens.add(token_found);
					content = content.substring(matcher.end());
					isfound = true;
					break;
				}
				
			}
			if(!isfound){
				tokens.add(new Token("Not a valid token", content.substring(0, 1)));
				content = content.substring(1);
			}
			
		}
		
		return tokens;
	}
	
	
	public static void main(String[] args) throws IOException {
		initRules();
		Helper h = new Helper();
		String content = h.read_file_content("test.txt");
		
		ArrayList<Token> tokens = get_tokens(content);
		
		// in case if you want to print tokens
		h.print_tokens(tokens);
		h.write_file_content(tokens, "output.txt");
		
	}

}
