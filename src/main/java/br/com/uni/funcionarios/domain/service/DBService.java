package br.com.uni.funcionarios.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uni.funcionarios.domain.model.Endereco;
import br.com.uni.funcionarios.domain.model.Funcionario;
import br.com.uni.funcionarios.domain.repository.FuncionarioRepository;

@Service
public class DBService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	//Carregando dados para teste
	
	public void instanciaDB() {
		
		Endereco endereco = new Endereco();
		endereco.setBairro("Lapa");
		endereco.setCep("11740-000");
		endereco.setCidade("São Paulo");
		endereco.setLogradouro("Rua Clélia");
		endereco.setNumero("1243");
		
		Funcionario f1 = new Funcionario();
		f1.setNome("José Silva");
		f1.setDataNascimento(LocalDate.now());
		f1.setSalario(new BigDecimal(400));
		f1.setTelefone("13 991655284");
		f1.setEndereco(endereco);
		f1.setCpf("717.717.372-20");
		
		Funcionario f2 = new Funcionario();
		f2.setNome("Maria Sé");
		f2.setDataNascimento(LocalDate.now());
		f2.setSalario(new BigDecimal(800.01));
		f2.setTelefone("13 991655284");
		f2.setEndereco(endereco);
		f2.setCpf("402.456.858-20");
		
		Funcionario f3 = new Funcionario();
		f3.setNome("Jordana Vieira");
		f3.setDataNascimento(LocalDate.now());
		f3.setSalario(new BigDecimal(2000));
		f3.setTelefone("13 991655284");
		f3.setEndereco(endereco);
		f3.setCpf("379.388.824-02");
		
		funcionarioRepository.saveAll(Arrays.asList(f1, f2, f3));
	}
}
