package com.celso.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.celso.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
