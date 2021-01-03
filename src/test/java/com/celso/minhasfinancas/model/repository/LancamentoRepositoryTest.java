package com.celso.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.celso.minhasfinancas.model.entity.Lancamento;
import com.celso.minhasfinancas.model.enums.StatusLancamento;
import com.celso.minhasfinancas.model.enums.TipoLancamento;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLacamento() {
		Lancamento lancamento = Lancamento.builder()
				                    .ano(2019)
				                    .mes(1)
				                    .descricao("Lancamento Mensal")
				                    .valor(BigDecimal.valueOf(10))
				                    .tipo(TipoLancamento.RECEITA)
				                    .status(StatusLancamento.PENDENTE)
				                    .dataCadastro(LocalDate.now())
				                    .build();
		
		lancamento = repository.save(lancamento);
		
		Assertions.assertThat(lancamento.getId()).isNotNull();
	}

}
	