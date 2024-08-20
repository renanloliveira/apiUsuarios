package br.com.cotiinformatica.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CriarUsuarioRequestDto {

	@NotEmpty(message = "Por favor, informe o nome do usuário.")
	@Size(min = 8, max = 100, message = "Por favor informe um nome de 8 a 100 caracteres. ")
	private String nome;

	@NotEmpty(message = "Por favor, informe o email do usuário.")
	@Email(message = "Por favor, informe um endeço de email válido.")
	private String email;

	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Por favor, informe a senha com letras minúsculas, maiúsculas, números, símbolos e pelo menos 8 caracteres.")
	@NotEmpty(message = "Por favor, informe a senha do usuário.")
	private String senha;

}
