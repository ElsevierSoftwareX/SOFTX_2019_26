package br.com.sdum.model;

import javax.validation.constraints.NotNull;

public class Login {
	@NotNull(message = "Telefone é obrigatório.")
	private String telefone;

	@NotNull(message = "Senha é obrigatório.")
	private String password;

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString() {
		
		return "Telefone: " + this.telefone + " - Senha:" + this.password;
		
	}
}
