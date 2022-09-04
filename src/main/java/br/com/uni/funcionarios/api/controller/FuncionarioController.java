package br.com.uni.funcionarios.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.uni.funcionarios.api.assembler.FuncionarioDTOAssembler;
import br.com.uni.funcionarios.api.disassembler.FuncionarioInputDTODisassembler;
import br.com.uni.funcionarios.api.dto.input.FuncionarioInputDTO;
import br.com.uni.funcionarios.api.dto.output.FuncionarioDTO;
import br.com.uni.funcionarios.api.dto.output.FuncionarioResumoDTO;
import br.com.uni.funcionarios.api.dto.output.NovoSalarioDTO;
import br.com.uni.funcionarios.domain.model.Funcionario;
import br.com.uni.funcionarios.domain.service.FuncionarioService;

@RestController
@RequestMapping(value = "/api/funcionarios")
public class FuncionarioController {

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private FuncionarioDTOAssembler funcionarioDTOAssembler;

	@Autowired
	private FuncionarioInputDTODisassembler funcionarioInputDTODisassembler;

	@GetMapping
	public List<FuncionarioResumoDTO> listar() {
		List<Funcionario> todosFuncionarios = funcionarioService.listar();
		return funcionarioDTOAssembler.toCollectionModel(todosFuncionarios);
	}

	@GetMapping("/{funcionarioId}")
	public FuncionarioDTO buscar(@PathVariable Long funcionarioId) {
		Funcionario funcionario = funcionarioService.buscarOuFalhar(funcionarioId);
		return funcionarioDTOAssembler.toFuncionarioModel(funcionario);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FuncionarioDTO adicionar(@RequestBody @Valid FuncionarioInputDTO funcionarioInputDTO) {
		Funcionario funcionario = funcionarioInputDTODisassembler.toDomainObject(funcionarioInputDTO);

		funcionario = funcionarioService.salvar(funcionario);

		return funcionarioDTOAssembler.toFuncionarioModel(funcionario);
	}

	@PutMapping("/{funcionarioId}")
	public FuncionarioDTO atualizar(@PathVariable Long funcionarioId,
			@RequestBody @Valid FuncionarioInputDTO funcionarioInputDTO) {
		Funcionario funcionarioAtual = funcionarioService.buscarOuFalhar(funcionarioId);

		funcionarioInputDTODisassembler.copyToDomainObject(funcionarioInputDTO, funcionarioAtual);

		funcionarioAtual = funcionarioService.salvar(funcionarioAtual);

		return funcionarioDTOAssembler.toFuncionarioModel(funcionarioAtual);
	}

	@DeleteMapping("/{funcionarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long funcionarioId) {
		funcionarioService.excluir(funcionarioId);
	}
	
	@PutMapping("/novo-salario/{funcionarioCpf}")
	public NovoSalarioDTO novoSalario(@PathVariable String funcionarioCpf) {
		return funcionarioService.calcularNovoSalario(funcionarioCpf);
	}
}
