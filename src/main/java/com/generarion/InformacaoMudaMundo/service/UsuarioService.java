package com.generarion.InformacaoMudaMundo.service;

import java.nio.charset.Charset;
//import java.util.Base64;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generarion.InformacaoMudaMundo.model.Usuario;
import com.generarion.InformacaoMudaMundo.model.UsuarioLogin;
import com.generarion.InformacaoMudaMundo.repository.UsuarioRepository;

@Service
public class UsuarioService {

	
	@Autowired
	private UsuarioRepository UsuRe;
	
	public Optional<Usuario> cadastrarUsuario(Usuario usuario){
		
		if(UsuRe.findByNome(usuario.getEmail())
				.isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario já existe",null);
		
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		return Optional.of(UsuRe.save(usuario));										
	}
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario){
		
		if(UsuRe.findById(usuario.getId()).isPresent()) {
			Optional<Usuario> buscaUsuario = UsuRe.
					findByNome(usuario.getEmail());
			if (buscaUsuario.isPresent()) {
				if(buscaUsuario.get().getId() !=usuario.getId())
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario Ja existe", null);
			}
			
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			
			return Optional.of(UsuRe.save(usuario));
		}
		
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado", null);
		
	}
	
	public Optional<UsuarioLogin> logarUsuario(Optional<UsuarioLogin> usuarioLogin) {
		
		Optional<Usuario> usuario = UsuRe.findByNome(usuarioLogin.get().getEmail());
		
		if (usuario.isPresent()) {
			if(compararSenhas(usuarioLogin.get().getSenha(),usuario.get().getSenha())) {
				
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getEmail(),
				usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				
				return usuarioLogin;
			}
		}
		
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario ou senha invalidos", null);
	}
	
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String senhaEncoder = encoder.encode(senha);
		
		return senhaEncoder;
	}
	
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(senhaDigitada, senhaBanco);				
	}

	private  String  gerarBasicToken(String  email, String  password) {
		String estrutura = email + ":" + password;
		byte[] estruturaBase64 = Base64.encodeBase64(
		estrutura.getBytes(Charset.forName("US-ASCII")));
		return  "Basic " + new  String(estruturaBase64);
	}
	
//	private String gerarBasicToken(String email,String password) {
//		
//		String estrutura = email + ":" + password;
//		byte[] estruturaBase64 = Base64.encodeBase64(estrutura.getBytes(Charset.forName("US-ASCII")));
//		return "basic " + new String(estruturaBase64);
//				
//	}
	
}