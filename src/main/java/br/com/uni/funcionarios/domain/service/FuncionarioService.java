package br.com.uni.funcionarios.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uni.funcionarios.api.dto.output.ImpostoDTO;
import br.com.uni.funcionarios.api.dto.output.NovoSalarioDTO;
import br.com.uni.funcionarios.domain.exception.FuncionarioNaoEncontradoException;
import br.com.uni.funcionarios.domain.exception.NegocioException;
import br.com.uni.funcionarios.domain.model.Funcionario;
import br.com.uni.funcionarios.domain.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	public List<Funcionario> listar() {
		return funcionarioRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@Transactional
	public Funcionario salvar(Funcionario funcionario) {
		try {

			validarCpf(funcionario);
			return funcionarioRepository.save(funcionario);

		} catch (ConstraintViolationException e) {
			throw new NegocioException(String.format("O CPF de número: %s é inválido, verifique o número novamente!",
					funcionario.getCpf()));
		}
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

	@Transactional
	public NovoSalarioDTO calcularNovoSalario(String funcionarioCpf) {
		Optional<Funcionario> funcionario = funcionarioRepository.findByCpf(funcionarioCpf);

		if (!funcionario.isPresent()) {
			throw new NegocioException(
					String.format("Não existe um funcionário cadastrado com o CPF: %s", funcionarioCpf));
		}

		BigDecimal salario = funcionario.get().getSalario();
		Double valor = funcionario.get().definirValorPercentual();
		BigDecimal reajuste = calcularReajuste(funcionario, valor);
		String percentual = calcularPercentual(funcionario, valor);

		salario = funcionario.get().calcularNovoSalario(reajuste);

		NovoSalarioDTO dto = new NovoSalarioDTO();
		dto.setCpf(funcionario.get().getCpf());
		dto.setNovoSalario(salario);
		dto.setPercentual(percentual);
		dto.setReajuste(reajuste);
		
		funcionario.get().setSalario(salario);

		return dto;
	}
	
	public ImpostoDTO calcularImposto(String funcionarioCpf) {
		Optional<Funcionario> funcionario = funcionarioRepository.findByCpf(funcionarioCpf);

		if (!funcionario.isPresent()) {
			throw new NegocioException(
					String.format("Não existe um funcionário cadastrado com o CPF: %s", funcionarioCpf));
		}
		
		String impostoCalculado = funcionario.get().calcularImposto();
		
		ImpostoDTO dto = new ImpostoDTO();
		dto.setCpf(funcionario.get().getCpf());
		dto.setImposto(impostoCalculado);
		
		return dto;
	}

	private BigDecimal calcularReajuste(Optional<Funcionario> funcionario, Double valor) {
		return funcionario.get().calcularReajuste(valor);
	}

	private String calcularPercentual(Optional<Funcionario> funcionario, Double valor) {
		return funcionario.get().calcularPercentual(valor);
	}

	private void validarCpf(Funcionario funcionario) {

		Optional<Funcionario> funcionarioExistente = funcionarioRepository.findByCpf(funcionario.getCpf());

		if (funcionarioExistente.isPresent() && !funcionarioExistente.get().equals(funcionario)) {
			throw new NegocioException(
					String.format("Já existe um funcionário cadastrado com o CPF: %s", funcionario.getCpf()));
		}
	}

}
