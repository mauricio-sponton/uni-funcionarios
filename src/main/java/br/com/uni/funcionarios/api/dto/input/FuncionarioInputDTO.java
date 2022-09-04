package br.com.uni.funcionarios.api.dto.input;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.stereotype.Component;

@Component
public class FuncionarioInputDTO {

	@NotBlank
	private String nome;

	@NotBlank
	private String cpf;

	@PastOrPresent
	private LocalDate dataNascimento;

	private String telefone;

	@NotNull
	@PositiveOrZero
	private BigDecimal salario;

	@Valid
	@NotNull
	private EnderecoInputDTO endereco;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public EnderecoInputDTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoInputDTO endereco) {
		this.endereco = endereco;
	}

}
