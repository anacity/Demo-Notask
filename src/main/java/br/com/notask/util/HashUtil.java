package br.com.notask.util;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

public class HashUtil {
	
	public static String hash(String palavra) {
		//tempero do hash
		String salt = "YmFsZWlhZmx1dHVhbnRlZGViYW1idQ==";
		//adiciona o tempero a palavra
		palavra = salt + palavra;
		//gerar hash
		String hash = Hashing.sha256().hashString(palavra, StandardCharsets.UTF_8).toString();
		//retornar o hash
		return hash;
	}
 
}

