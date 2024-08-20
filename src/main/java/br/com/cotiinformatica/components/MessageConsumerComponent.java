package br.com.cotiinformatica.components;

import java.util.UUID;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.cotiinformatica.collections.LogMensageria;
import br.com.cotiinformatica.dtos.MensagemUsuarioDto;
import br.com.cotiinformatica.repositories.LogMensageriaRepository;

@Component
public class MessageConsumerComponent {
	@Autowired
	private EmailComponent emailComponent;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private LogMensageriaRepository logMensageriaRepository;

	/*
	 * Método para ler e processar o conteúdo da fila
	 */
	@RabbitListener(queues = { "usuarios" })
	public void receive(@Payload String payload) throws Exception {
//deserializar os dados de JSON para objeto Java
		MensagemUsuarioDto dto = objectMapper.readValue(payload, MensagemUsuarioDto.class);
		LogMensageria logMensageria = new LogMensageria();
		logMensageria.setId(UUID.randomUUID());
		logMensageria.setUsuario(dto.getEmailUsuario());
		try {
//realizando o envio do email
			emailComponent.send(dto.getEmailUsuario(), dto.getAssunto(), dto.getMensagem());
			logMensageria.setStatus("SUCESSO");
			logMensageria.setDescricao("Email de boas vindas enviado com sucesso.");
		} catch (Exception e) {
			logMensageria.setStatus("ERRO");
			logMensageria.setDescricao(e.getMessage());
		} finally {
			logMensageriaRepository.save(logMensageria);
		}
	}
}