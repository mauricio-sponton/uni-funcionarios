package br.com.uni.funcionarios.api.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.uni.funcionarios.api.model.input.FuncionarioInput;
import br.com.uni.funcionarios.domain.model.Funcionario;

@Component
public class FuncionarioInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public Funcionario toDomainObject(FuncionarioInput funcionarioInput) {
        return modelMapper.map(funcionarioInput, Funcionario.class);
    }
    
    public void copyToDomainObject(FuncionarioInput funcionarioInput, Funcionario funcionario) {
        modelMapper.map(funcionarioInput, funcionario);
    }   
} 