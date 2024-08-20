package br.com.cotiinformatica.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.components.CryptoComponent;
import br.com.cotiinformatica.components.MessageProducerComponent;
import br.com.cotiinformatica.components.TokenComponent;
import br.com.cotiinformatica.dtos.AutenticarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.AutenticarUsuarioResponseDto;
import br.com.cotiinformatica.dtos.CriarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.CriarUsuarioResponseDto;
import br.com.cotiinformatica.dtos.MensagemUsuarioDto;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CryptoComponent cryptoComponent;
	
	@Autowired
	private TokenComponent tokenComponent;
	
	@Autowired
	private MessageProducerComponent messageProducerComponent;
	

	public CriarUsuarioResponseDto criarUsuario(CriarUsuarioRequestDto request) throws Exception {

		if (usuarioRepository.existsByEmail(request.getEmail())) {
			// lançar uma exceção no projeto (erro)
			throw new IllegalArgumentException("O email informado já está cadastrado, tente outro.");

		}
		Usuario usuario = new Usuario();

		usuario.setId(UUID.randomUUID());
		usuario.setNome(request.getNome());
		usuario.setEmail(request.getEmail());
		usuario.setSenha(cryptoComponent.encryptSHA256(request.getSenha()));

		usuarioRepository.save(usuario);
		
		MensagemUsuarioDto dto = new MensagemUsuarioDto();
		dto.setNomeUsuario(usuario.getNome());
		dto.setEmailUsuario(usuario.getEmail());
		dto.setAssunto("Cadastro realizado com sucesso - COTI informática");
		dto.setMensagem("Olá " + usuario.getNome() + "! Seja bem vindo ao sistema!\n\nAtt\nEquipe Coti" );
		
		messageProducerComponent.sendMessage(dto);

		CriarUsuarioResponseDto response = new CriarUsuarioResponseDto();

		response.setId(usuario.getId());
		response.setNome(usuario.getNome());
		response.setEmail(usuario.getEmail());
		response.setDataHoraCadastro(new Date());

		return response;

	}

	public AutenticarUsuarioResponseDto autenticarUsuario(AutenticarUsuarioRequestDto request) throws Exception {

		request.setSenha(cryptoComponent.encryptSHA256(request.getSenha()));

		Usuario usuario = usuarioRepository.findByEmailAndSenha(request.getEmail(), request.getSenha());

		if (usuario != null) {

			AutenticarUsuarioResponseDto response = new AutenticarUsuarioResponseDto();
			
			response.setId(usuario.getId());
			response.setNome(usuario.getNome());
			response.setEmail(usuario.getEmail());
			response.setDataHoraAcesso(new Date());
			response.setDataHoraExpiracao(tokenComponent.getExpiration()); 
			response.setToken(tokenComponent.generateToken(usuario)); 

			return response;
			
			
		} else {
			throw new IllegalArgumentException("Usuário nao encontrado, verifique os dados informados.");
		}

	}
}
