package br.com.cotiinformatica.repositories;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.entities.Usuario;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

	/*
	* Query Method para executar uma consulta SQL como:
	* SELECT COUNT(email) FROM usuario WHERE email = ?
	*/
	
	boolean existsByEmail(String email);
	
	
	
	/*
	* Query Method para executar uma consulta SQL como:
	* SELECT * FROM usuario WHERE email = ? AND senha = ?
	*/
	
	Usuario findByEmailAndSenha(String email , String senha);
}
