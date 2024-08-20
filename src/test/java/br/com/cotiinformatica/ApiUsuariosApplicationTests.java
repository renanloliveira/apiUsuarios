package br.com.cotiinformatica;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.cotiinformatica.dtos.AutenticarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.CriarUsuarioRequestDto;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiUsuariosApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static String emailUsuario;

	@Test
	@Order(1)
	void criarUsuarioComSuceso_test() throws Exception {

		CriarUsuarioRequestDto request = new CriarUsuarioRequestDto();
		Faker faker = new Faker();

		request.setNome(faker.name().fullName());
		request.setEmail(faker.internet().emailAddress());
		request.setSenha("@Teste2024");

		mockMvc.perform(post("/api/usuarios/criar").contentType("application/json")
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk());

		emailUsuario = request.getEmail();

	}

	@Test
	@Order(2)
	void criarUsuarioComEmailJaExistente_test() throws Exception {
		CriarUsuarioRequestDto request = new CriarUsuarioRequestDto();
		Faker faker = new Faker();

		request.setNome(faker.name().fullName());
		request.setEmail(emailUsuario);
		request.setSenha("@Teste2024");

		MvcResult mvcResult = mockMvc
				.perform(post("/api/usuarios/criar").contentType("application/json")
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest()).andReturn();

		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

		assertTrue(content.contains("O email informado já está cadastrado, tente outro."));

	}

	@Test
	@Order(3)
	void criarUsuarioComASenhaFraca_test() throws Exception {
		CriarUsuarioRequestDto request = new CriarUsuarioRequestDto();
		Faker faker = new Faker();

		request.setNome(faker.name().fullName());
		request.setEmail(faker.internet().emailAddress());
		request.setSenha("teste2024");

		MvcResult mvcResult = mockMvc
				.perform(post("/api/usuarios/criar").contentType("application/json")
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest()).andReturn();

		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

		assertTrue(content.contains(
				"Por favor, informe a senha com letras minúsculas, maiúsculas, números, símbolos e pelo menos 8 caracteres."));

	}

	@Test
	@Order(4)
	void criarUsuarioDeValidacaoComCamposObrigatorios_test() throws Exception {
		CriarUsuarioRequestDto request = new CriarUsuarioRequestDto();

		MvcResult mvcResult = mockMvc
				.perform(post("/api/usuarios/criar").contentType("application/json")
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest()).andReturn();

		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

		assertTrue(content.contains("Por favor, informe o nome do usuário."));
		assertTrue(content.contains("Por favor, informe o email do usuário."));
		assertTrue(content.contains("Por favor, informe a senha do usuário."));

	}

	@Test
	@Order(5)
	void autenticarUsuarioComSucesso_test() throws Exception {

		AutenticarUsuarioRequestDto request = new AutenticarUsuarioRequestDto();

		request.setEmail(emailUsuario);
		request.setSenha("@Teste2024");

		mockMvc.perform(post("/api/usuarios/autenticar").contentType("application/json")
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk());

	}

	@Test
	@Order(6)
	void autenticarUsuarioAcessoNegado_test() throws Exception {
		AutenticarUsuarioRequestDto request = new AutenticarUsuarioRequestDto();

		request.setEmail(emailUsuario);
		request.setSenha("@Teste1234");

		MvcResult mvcResult = mockMvc
				.perform(post("/api/usuarios/autenticar").contentType("application/json")
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest()).andReturn();

		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains("Usuário nao encontrado, verifique os dados informados."));

	}
}
