package br.com.uni.funcionarios.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.uni.funcionarios.api.dto.output.FuncionarioDTO;
import br.com.uni.funcionarios.api.dto.output.FuncionarioResumoDTO;
import br.com.uni.funcionarios.domain.model.Funcionario;

@Component
public class FuncionarioDTOAssembler {

	@Autowired
	private ModelMapper modelMapper;

	public FuncionarioDTO toFuncionarioModel(Funcionario funcionario) {
		return modelMapper.map(funcionario, FuncionarioDTO.class);
	}

	public FuncionarioResumoDTO toFuncionarioResumoModel(Funcionario funcionario) {
		return modelMapper.map(funcionario, FuncionarioResumoDTO.class);
	}

	public List<FuncionarioResumoDTO> toCollectionModel(List<Funcionario> funcionarios) {
		return funcionarios.stream().map(funcionario -> toFuncionarioResumoModel(funcionario))
				.collect(Collectors.toList());
	}

}
