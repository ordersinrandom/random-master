package com.jbp.randommaster.draft.hashing;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class AlgorithmTest1 {

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		List<String> result=new LinkedList<String>();
		
		
		String[] msg = new String[] { "My message 123", "My message 123 safdsfdsauisaucsuichdscsdcsad 4",
				"My message 123" };
		
		for (String m : msg) {
			MessageDigest di = MessageDigest.getInstance("SHA1");
			byte[] b = di.digest(m.getBytes("ISO-8859-1"));
			result.add(Base64.encodeBase64String(b));
		}
		
	
		for (String s : result) {
			System.out.println(s+" - length = "+s.length());
		}
		

	}

}
