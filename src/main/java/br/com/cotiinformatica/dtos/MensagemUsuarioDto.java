package br.com.cotiinformatica.dtos;

import lombok.Data;

@Data
public class MensagemUsuarioDto {
	
	
	private String nomeUsuario;
	private String emailUsuario;
	private String mensagem;
	private String assunto;

}
