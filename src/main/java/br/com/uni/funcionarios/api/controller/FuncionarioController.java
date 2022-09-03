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

import br.com.uni.funcionarios.api.assembler.FuncionarioModelAssembler;
import br.com.uni.funcionarios.api.disassembler.FuncionarioInputDisassembler;
import br.com.uni.funcionarios.api.model.input.FuncionarioInput;
import br.com.uni.funcionarios.api.model.output.FuncionarioModel;
import br.com.uni.funcionarios.api.model.output.FuncionarioResumoModel;
import br.com.uni.funcionarios.domain.model.Funcionario;
import br.com.uni.funcionarios.domain.service.FuncionarioService;

@RestController
@RequestMapping(value = "/api/funcionarios")
public class FuncionarioController {

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private FuncionarioModelAssembler funcionarioModelAssembler;

	@Autowired
	private FuncionarioInputDisassembler funcionarioInputDisassembler;

	@GetMapping
	public List<FuncionarioResumoModel> listar() {
		List<Funcionario> todosFuncionarios = funcionarioService.listar();
		return funcionarioModelAssembler.toCollectionModel(todosFuncionarios);
	}

	@GetMapping("/{funcionarioId}")
	public FuncionarioModel buscar(@PathVariable Long funcionarioId) {
		Funcionario funcionario = funcionarioService.buscarOuFalhar(funcionarioId);
		return funcionarioModelAssembler.toFuncionarioModel(funcionario);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FuncionarioModel adicionar(@RequestBody @Valid FuncionarioInput funcionarioInput) {
		Funcionario funcionario = funcionarioInputDisassembler.toDomainObject(funcionarioInput);

		funcionario = funcionarioService.salvar(funcionario);

		return funcionarioModelAssembler.toFuncionarioModel(funcionario);
	}

	@PutMapping("/{funcionarioId}")
	public FuncionarioModel atualizar(@PathVariable Long funcionarioId,
			@RequestBody @Valid FuncionarioInput funcionarioInput) {
		Funcionario funcionarioAtual = funcionarioService.buscarOuFalhar(funcionarioId);

		funcionarioInputDisassembler.copyToDomainObject(funcionarioInput, funcionarioAtual);

		funcionarioAtual = funcionarioService.salvar(funcionarioAtual);

		return funcionarioModelAssembler.toFuncionarioModel(funcionarioAtual);
	}

	@DeleteMapping("/{funcionarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long funcionarioId) {
		funcionarioService.excluir(funcionarioId);
	}
}
