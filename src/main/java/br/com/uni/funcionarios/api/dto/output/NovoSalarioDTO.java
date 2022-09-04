package br.com.uni.funcionarios.api.dto.output;

import java.math.BigDecimal;

public class NovoSalarioDTO {

	private String cpf;
	private BigDecimal novoSalario;
	private BigDecimal reajuste;
	private String percentual;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public BigDecimal getNovoSalario() {
		return novoSalario;
	}

	public void setNovoSalario(BigDecimal novoSalario) {
		this.novoSalario = novoSalario;
	}

	public BigDecimal getReajuste() {
		return reajuste;
	}

	public void setReajuste(BigDecimal reajuste) {
		this.reajuste = reajuste;
	}

	public String getPercentual() {
		return percentual;
	}

	public void setPercentual(String percentual) {
		this.percentual = percentual;
	}

}
