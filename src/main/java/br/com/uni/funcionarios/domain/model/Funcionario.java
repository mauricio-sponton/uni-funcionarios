package br.com.uni.funcionarios.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Funcionario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	@CPF
	@Column(unique = true)
	private String cpf;
	
	@Column(columnDefinition = "date")
	private LocalDate dataNascimento;
	
	private String telefone;
	
	@Embedded
	private Endereco endereco;
	
	private BigDecimal salario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}
	
	public BigDecimal calcularReajuste(Double valorReajuste) {
		return salario.multiply(new BigDecimal(valorReajuste)).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public BigDecimal calcularNovoSalario(BigDecimal reajuste) {
		return salario.add(reajuste);
	}
	
	public String calcularPercentual(Double percentual) {
		percentual = (percentual * 100);
		return String.format("%.0f", percentual).concat("%");
	}
	
	public Double definirValorPercentual() {
		
		Double valor = null;
		if(salario.compareTo(BigDecimal.valueOf(0)) > 0 && salario.compareTo(BigDecimal.valueOf(400)) <= 0) {
			valor = 0.15;
		}
		else if(salario.compareTo(BigDecimal.valueOf(400)) > 0 && salario.compareTo(BigDecimal.valueOf(800)) <= 0) {
			valor = 0.12;
		}
		else if(salario.compareTo(BigDecimal.valueOf(800)) > 0 && salario.compareTo(BigDecimal.valueOf(1200)) <= 0) {
			valor = 0.10;
		}
		else if(salario.compareTo(BigDecimal.valueOf(1200)) > 0 && salario.compareTo(BigDecimal.valueOf(2000)) <= 0) {
			valor = 0.07;
		}
		else {
			valor = 0.04;
		}
		
		return valor;
	}
	
	public String calcularImposto() {
		
		BigDecimal imposto = BigDecimal.ZERO;
		BigDecimal resto = BigDecimal.ZERO;
		
		if(salario.compareTo(BigDecimal.valueOf(4500)) > 0) {
			imposto = new BigDecimal((1000 * 0.08) + (1500 * 0.18));
			resto = salario.subtract(BigDecimal.valueOf(4500));
			imposto = imposto.add(resto.multiply(BigDecimal.valueOf(0.28)));
		}
		else if(salario.compareTo(BigDecimal.valueOf(3000)) > 0) {
			imposto = new BigDecimal(1000 * 0.08);
			resto = salario.subtract(BigDecimal.valueOf(3000));
			imposto = imposto.add(resto.multiply(BigDecimal.valueOf(0.18)));
		}
		
		else if(salario.compareTo(BigDecimal.valueOf(2000)) > 0) {
			resto = salario.subtract(BigDecimal.valueOf(2000));
			imposto = imposto.add(resto.multiply(BigDecimal.valueOf(0.08)));
		}
		
		
		if(imposto.compareTo(BigDecimal.valueOf(0)) > 0) {
			return "R$ " + imposto.setScale(2, RoundingMode.HALF_EVEN);
		}else {
			return "Isento";
		}
		
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Funcionario other = (Funcionario) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
