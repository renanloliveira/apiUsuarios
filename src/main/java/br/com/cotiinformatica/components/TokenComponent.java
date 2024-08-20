package br.com.cotiinformatica.components;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.cotiinformatica.entities.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenComponent {

	@Value("${jwt.secretkey}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private String expiration;

	/*
	 * Método para gerar o TOKEN do usuário
	 */
	public String generateToken(Usuario usuario) throws Exception {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", "USER");

		return Jwts.builder().setSubject(usuario.getEmail()).setNotBefore(new Date()).setExpiration(getExpiration())
				.addClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	/*
	 * Método para retornar a data e hora de expiração
	 */
	public Date getExpiration() throws Exception {
	Date dataAtual = new Date();
	
	return new Date(dataAtual.getTime() + Long.parseLong(expiration));
	}
}
