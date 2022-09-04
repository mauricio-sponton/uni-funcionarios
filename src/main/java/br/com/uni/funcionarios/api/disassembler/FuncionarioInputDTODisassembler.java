package br.com.uni.funcionarios.api.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.uni.funcionarios.api.dto.input.FuncionarioInputDTO;
import br.com.uni.funcionarios.domain.model.Funcionario;

@Component
public class FuncionarioInputDTODisassembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public Funcionario toDomainObject(FuncionarioInputDTO funcionarioInputDTO) {
        return modelMapper.map(funcionarioInputDTO, Funcionario.class);
    }
    
    public void copyToDomainObject(FuncionarioInputDTO funcionarioInputDTO, Funcionario funcionario) {
        modelMapper.map(funcionarioInputDTO, funcionario);
    }   
} 