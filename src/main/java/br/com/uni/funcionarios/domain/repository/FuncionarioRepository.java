package br.com.uni.funcionarios.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.uni.funcionarios.domain.model.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

	
	@Query("select f from Funcionario f where f.cpf = :cpf")
	Optional<Funcionario> findByCpf(String cpf);
}
