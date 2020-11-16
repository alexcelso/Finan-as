package com.celso.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.celso.minhasfinancas.exceptions.ErroAutenticacao;
import com.celso.minhasfinancas.exceptions.RegraNegocioException;
import com.celso.minhasfinancas.model.entity.Usuario;
import com.celso.minhasfinancas.model.repository.UsuarioRepository;
import com.celso.minhasfinancas.service.imple.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	@Before
	public void setUp() {
		service = new UsuarioServiceImpl(repository);		
	}
	
	@Test(expected = Test.None.class)
	public void deveSalvarUmUsuario() {		
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());		
		Usuario usuario =  Usuario.builder()
				.id(1L)
				.nome("usuario")
				.email("usuario@gmail.com")
				.senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificao		
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("usuario");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("usuario@gmail.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailCadastrado() {
		//cenario		
		String email = "usuario@gmail.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//acao
		service.salvarUsuario(usuario);
		
		//verificação
		Mockito.verify( repository, Mockito.never() ).save(usuario);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUsuarioComSucesso() {
		//Cenario
		String email = "usuario@gmail.com";
		String senha = "senha";
		
		Usuario usuario  = Usuario.builder().email(email).senha(senha).id(1L).build();
		Mockito.when(repository.findByEmail(email) ).thenReturn(Optional.of(usuario));
		
		//Ação		
		Usuario result = service.autenticar(email, senha);
		
		//verificação		
		Assertions.assertThat(result).isNotNull();
	}
		
	@Test
	public void deveLancarErroaQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao		
		 Throwable exception = Assertions.catchThrowable(() -> service.autenticar("usuario@gmail.com", "senha"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o e-mail informado!");
	}
	
	@Test
	public void deveLacarErroQuandoSenhaNaoBater() {
		//cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("usuario@gmail.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//ação		
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("usuario@gmail.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida!");
	}
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenario		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		//Acão		
		service.validarEmail("usuario@gmail.com");
	}	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarUmErroAoValidarEmailQuandoExistirUmEmailCadastrado() {
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//Acão
		service.validarEmail("paulo@gmail.com");		
	}

}
