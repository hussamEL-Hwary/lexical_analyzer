package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthStyle;

public class Helper {

	/**
	 * Reading file content and returning it as a string
	 * 
	 * @param filePath 
	 * @return string of the file content
	 * */
	public String read_file_content(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		StringBuilder content = new StringBuilder();
		String line = null;
		String ls = System.getProperty("line.separator");
		while((line=reader.readLine()) != null){
			content.append(line);
			content.append(ls);
		}
		reader.close();
		return content.toString();
	}
	
	
	/**
	 * Write result tokens to output file 
	 * 
	 * @param tokens
	 * @param filename
	 * @throws IOException
	 */
	public void write_file_content(ArrayList<Token> tokens, String filename) throws IOException{
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		for(Token t:tokens) {
			writer.write("<" + t.type + ">:  " + t.lexeme+System.lineSeparator());
		}
		writer.close();
	}
	
	
	/**
	 * print tokens in console
	 * 
	 * @param tokens
	 */
	public void print_tokens(ArrayList<Token> tokens) {
		for(Token t:tokens) {
			System.out.println(t.type+"     "+t.lexeme);
		}
	}
}
