package com.edplan.bilipixeldraw;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonUtils
{
	
	public static String read(BufferedReader in) throws IOException{
		StringBuilder sb=new StringBuilder();
		String s;
		while((s=in.readLine())!=null){
			sb.append(s);
		}
		return sb.toString();
	}
	
	public static String read(InputStream in) throws IOException{
		return read(new BufferedReader(new InputStreamReader(in)));
	}
	
}
