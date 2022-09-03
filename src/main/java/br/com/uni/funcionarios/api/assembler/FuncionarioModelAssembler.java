package br.com.uni.funcionarios.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.uni.funcionarios.api.model.output.FuncionarioModel;
import br.com.uni.funcionarios.api.model.output.FuncionarioResumoModel;
import br.com.uni.funcionarios.domain.model.Funcionario;

@Component
public class FuncionarioModelAssembler {

	@Autowired
	private ModelMapper modelMapper;

	public FuncionarioModel toFuncionarioModel(Funcionario funcionario) {
		return modelMapper.map(funcionario, FuncionarioModel.class);
	}

	public FuncionarioResumoModel toFuncionarioResumoModel(Funcionario funcionario) {
		return modelMapper.map(funcionario, FuncionarioResumoModel.class);
	}

	public List<FuncionarioResumoModel> toCollectionModel(List<Funcionario> funcionarios) {
		return funcionarios.stream().map(funcionario -> toFuncionarioResumoModel(funcionario))
				.collect(Collectors.toList());
	}

}
