package br.com.cotiinformatica.components;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class CryptoComponent {
	/*
	 * Método para criptografia em formato SHA256
	 */
	public String encryptSHA256(String input) {
		try {
			MessageDigest digest = MessageDigest

					.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException

				| java.io.UnsupportedEncodingException e) {

			throw new RuntimeException(e);
		}
	}
}