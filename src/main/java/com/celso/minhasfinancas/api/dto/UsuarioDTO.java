package com.celso.minhasfinancas.api.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
   
	private String email;
	private String nome;
	private String senha;
}
