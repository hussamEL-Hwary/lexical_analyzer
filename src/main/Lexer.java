package main;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import java.util.ArrayList;
import java.util.regex.Matcher;
import main.Helper;

public class Lexer {
	static ArrayList<Expression> regexRules = new ArrayList<Expression>();
	/**
	 * initialize regexRules with regular expressions
	 */
	public static void initRules(){
		regexRules.add(new Expression("MULTI_COMMENT", "/\\*(.|\\s)*\\*/"));
		regexRules.add(new Expression("LineComment","(//(.*?)[\r$]?\n)"));
		regexRules.add(new Expression("NewLine","(\\n)"));
		regexRules.add(new Expression("WhiteSpace","( ){1}"));
		regexRules.add(new Expression("LEFT_CURLY_B", "\\}{1}"));
		regexRules.add(new Expression("RIGHT_CURLY_B", "\\{{1}"));
		regexRules.add(new Expression("LEFT_SQUARE_B", "\\]{1}"));
		regexRules.add(new Expression("RIGHT_SQUARE_B", "\\[{1}"));
		regexRules.add(new Expression("LEFT_ROUND_B", "\\){1}"));
		regexRules.add(new Expression("RIGHT_ROUND_B", "\\({1}"));
		regexRules.add(new Expression("COMMA", "\\,{1}"));
		regexRules.add(new Expression("SEMICOLON", "\\;{1}"));
		regexRules.add(new Expression("DOT", "\\.{1}"));
		regexRules.add(new Expression("PLUS", "(\\+{1} ^+)"));
		regexRules.add(new Expression("PREPROCESSOR", "\\#{1}"));
		regexRules.add(new Expression("BACKWARD_SLASH", "\\\'{1}"));
		regexRules.add(new Expression("INTEGER_LITERAL", "\\b(\\d{1,9})\\b"));
		regexRules.add(new Expression("DOUBLE_LITERAL", "(?<=\\s|^)[-+]?\\d+(\\.)\\d+(?=\\s|$|;)"));
		regexRules.add(new Expression("INCREMENT", "\\++{2}"));
		regexRules.add(new Expression("DECREMENT", "\\--{2}"));
		regexRules.add(new Expression("MINUS", "(\\-{1})"));
		regexRules.add(new Expression("MULTIPLAY", "\\*{1}"));
		regexRules.add(new Expression("Division", "\\/{1}"));
		regexRules.add(new Expression("Assigment", "(=){1} ^="));
		regexRules.add(new Expression("Mod", "\\%{1}"));
		regexRules.add(new Expression("EQUAL", "(=){1}"));
		regexRules.add(new Expression("NOTEQUAL", "\\!={1}"));
		regexRules.add(new Expression("GREATER THAN", "\\>{1}[^=]"));
		regexRules.add(new Expression("LESS THAN", "(<){1}[^=]"));
		regexRules.add(new Expression("GREATER_EQL", "(>=){1}"));
		regexRules.add(new Expression("LESS_EQL", "(<=){1}"));
		regexRules.add(new Expression("BITWISE_AND", "(&){1}[^\\&]"));
		regexRules.add(new Expression("BITWISE_OR", "(\\|){1}[^\\|]"));
		regexRules.add(new Expression("BITWISE_XOR", "(\\^){1}"));
		regexRules.add(new Expression("BITWISE_NOT", "(\\~){1}"));
		regexRules.add(new Expression("RIGHT_SHIFT", "(\\<){2}"));
		regexRules.add(new Expression("LEFT_SHIFT", "(\\>){2}"));
		regexRules.add(new Expression("AND", "(\\&){2}"));
		regexRules.add(new Expression("OR", "(\\|){2}"));
		regexRules.add(new Expression("NOT", "(\\!){1}[^=]"));
		
		/*KEYWORDS*/
		regexRules.add(new Expression("INT","\\b(int)\\b"));
		regexRules.add(new Expression("AUTO","\\b(auto)\\b"));
		regexRules.add(new Expression("NEW","\\b(new)\\b"));
		regexRules.add(new Expression("TRUE","\\b(true)\\b"));
		regexRules.add(new Expression("FALSE","\\b(break)\\b"));
		regexRules.add(new Expression("BOOL","\\b(bool)\\b"));
		regexRules.add(new Expression("CASE","\\b(case)\\b"));
		regexRules.add(new Expression("CHAR","\\b(char)\\b"));
		regexRules.add(new Expression("CONST","\\b(const)\\b"));
		regexRules.add(new Expression("CONTINUE","\\b(continue)\\b"));
		regexRules.add(new Expression("DEFAULT","\\b(default)\\b"));
		regexRules.add(new Expression("DO","\\b(do)\\b"));
		regexRules.add(new Expression("DOUBLE","\\b(double)\\b"));
		regexRules.add(new Expression("ELSE","\\b(else)\\b"));
		regexRules.add(new Expression("ENUM","\\b(enum)\\b"));
		regexRules.add(new Expression("EXTERN","\\b(extern)\\b"));
		regexRules.add(new Expression("FLOAT","\\b(float)\\b"));
		regexRules.add(new Expression("FOR","\\b(for)\\b"));
		regexRules.add(new Expression("GOTO","\\b(goto)\\b"));
		regexRules.add(new Expression("IF","\\b(if)\\b"));
		regexRules.add(new Expression("LONG","\\b(long)\\b"));
		regexRules.add(new Expression("REGISTER","\\b(register)\\b"));
		regexRules.add(new Expression("RETURN","\\b(return)\\b"));
		regexRules.add(new Expression("SHORT","\\b(short)\\b"));
		regexRules.add(new Expression("SIGNED","\\b(signed)\\b"));
		regexRules.add(new Expression("SIZEOF","\\b(sizeof)\\b"));
		regexRules.add(new Expression("STATIC","\\b(static)\\b"));
		regexRules.add(new Expression("STRUCT","\\b(struct)\\b"));
		regexRules.add(new Expression("SWITCH","\\b(switch)\\b"));
		regexRules.add(new Expression("TYPEDEF","\\b(typedef)\\b"));
		regexRules.add(new Expression("UNION","\\b(union)\\b"));
		regexRules.add(new Expression("UNSIGNED","\\b(unsigned)\\b"));
		regexRules.add(new Expression("WHILE","\\b(while)\\b"));
		regexRules.add(new Expression("VOLATILE","\\b(volatile)\\b"));
		
		regexRules.add(new Expression("IDENTIFIER","\\b([a-zA-Z_]{1}[0-9a-zA-Z_]{0,31})"));
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
