package br.com.uni.funcionarios.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uni.funcionarios.domain.exception.FuncionarioNaoEncontradoException;
import br.com.uni.funcionarios.domain.model.Funcionario;
import br.com.uni.funcionarios.domain.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	public List<Funcionario> listar() {
		return funcionarioRepository.findAll();
	}

	@Transactional
	public Funcionario salvar(Funcionario funcionario) {
		return funcionarioRepository.save(funcionario);
	}

	public Funcionario buscarOuFalhar(Long funcionarioId) {
		return funcionarioRepository.findById(funcionarioId)
				.orElseThrow(() -> new FuncionarioNaoEncontradoException(funcionarioId));
	}

	@Transactional
	public void excluir(Long funcionarioId) {

		try {

			funcionarioRepository.deleteById(funcionarioId);

		} catch (EmptyResultDataAccessException e) {
			throw new FuncionarioNaoEncontradoException(funcionarioId);
		}
	}

}
