package br.com.uni.funcionarios.api.model.output;

public class FuncionarioModel extends FuncionarioResumoModel {

	private EnderecoModel endereco;

	public EnderecoModel getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoModel endereco) {
		this.endereco = endereco;
	}

}
