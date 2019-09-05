package br.com.sdum.enums;

public enum Roles {

	ON ("On"),  OFF ("Off");

	private final String nome;

	Roles (String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}	
}