package br.com.uni.funcionarios.api.dto.output;

public class FuncionarioDTO extends FuncionarioResumoDTO {

	private EnderecoDTO endereco;

	public EnderecoDTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoDTO endereco) {
		this.endereco = endereco;
	}

}
