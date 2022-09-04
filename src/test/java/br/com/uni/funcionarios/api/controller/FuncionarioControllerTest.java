package br.com.uni.funcionarios.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.uni.funcionarios.api.assembler.FuncionarioDTOAssembler;
import br.com.uni.funcionarios.api.disassembler.FuncionarioInputDTODisassembler;
import br.com.uni.funcionarios.api.dto.input.FuncionarioInputDTO;
import br.com.uni.funcionarios.api.dto.output.FuncionarioDTO;
import br.com.uni.funcionarios.api.dto.output.FuncionarioResumoDTO;
import br.com.uni.funcionarios.api.dto.output.ImpostoDTO;
import br.com.uni.funcionarios.api.dto.output.NovoSalarioDTO;
import br.com.uni.funcionarios.domain.model.Endereco;
import br.com.uni.funcionarios.domain.model.Funcionario;
import br.com.uni.funcionarios.domain.service.FuncionarioService;

@SpringBootTest
class FuncionarioControllerTest {
	
	private static final BigDecimal SALARIO = new BigDecimal(400);

	private static final String TELEFONE = "13991655284";

	private static final LocalDate DATA_NASCIMENTO = LocalDate.now();

	private static final String CPF = "200.584.322-99";

	private static final String NOME = "Luiz";

	private static final long ID = 1L;
	
	@InjectMocks
	private FuncionarioController controller;
	
	@Mock
	private FuncionarioService service;
	
	@Mock
	private FuncionarioDTOAssembler funcionarioDTOAssembler;
	
	@Mock
	private FuncionarioInputDTODisassembler disassembler;
	
	private Funcionario funcionario;
	private Endereco endereco;
	private NovoSalarioDTO dto;
	private ImpostoDTO impostoDTO;
	private FuncionarioDTO funcionarioDTO;
	private FuncionarioResumoDTO funcionarioResumoDTO;
	private FuncionarioInputDTO funcionarioInputDTO;
	

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		startFuncionario();
	}

	@Test
	void whenFindAll_thenReturnListFuncionarioDTO() {
		when(service.listar()).thenReturn(List.of(funcionario));
		when(funcionarioDTOAssembler.toCollectionModel(any())).thenReturn(List.of(funcionarioResumoDTO));
		
		List<FuncionarioResumoDTO> resposta = controller.listar();
		
		assertNotNull(resposta);
		assertEquals(FuncionarioResumoDTO.class, resposta.get(0).getClass());
		assertEquals(ID, resposta.get(0).getId());
	}

	@Test
	void whenFindById_thenReturnSuccess() {
		when(service.buscarOuFalhar(anyLong())).thenReturn(funcionario);
		when(funcionarioDTOAssembler.toFuncionarioModel(any())).thenReturn(funcionarioDTO);
		
		FuncionarioDTO resposta = controller.buscar(ID);
		
		assertNotNull(resposta);
		assertEquals(FuncionarioDTO.class, resposta.getClass());
		assertEquals(ID, resposta.getId());
	}

	@Test
	void whenAdicionar_thenReturnCreated() {
		when(service.salvar(any())).thenReturn(funcionario);
		when(disassembler.toDomainObject(any())).thenReturn(funcionario);
		when(funcionarioDTOAssembler.toFuncionarioModel(any())).thenReturn(funcionarioDTO);
		
		FuncionarioDTO resposta = controller.adicionar(funcionarioInputDTO);
		
		assertNotNull(resposta);
		assertEquals(ID, resposta.getId());
		
	}

	@Test
	void whenRemover_thenReturnSuccess() {
		doNothing().when(service).excluir(anyLong());
		controller.remover(ID);
		verify(service, times(1)).excluir(anyLong());
	}

	@Test
	void testNovoSalario() {
		when(service.calcularNovoSalario(anyString())).thenReturn(dto);
		
		NovoSalarioDTO resposta = controller.novoSalario(CPF);
		
		assertNotNull(resposta);
		assertEquals(CPF, resposta.getCpf());
		assertEquals(dto.getNovoSalario(), resposta.getNovoSalario());
		assertEquals(dto.getPercentual(), resposta.getPercentual());
		assertEquals(dto.getReajuste(), resposta.getReajuste());
	}

	@Test
	void testImposto() {
		when(service.calcularImposto(anyString())).thenReturn(impostoDTO);
		
		ImpostoDTO resposta = controller.imposto(CPF);
		assertNotNull(resposta);
		assertEquals(impostoDTO.getCpf(), resposta.getCpf());
		assertEquals(impostoDTO.getImposto(), resposta.getImposto());
		
	}
	
	private void startFuncionario() {

		endereco = new Endereco();
		endereco.setBairro("Lapa");
		endereco.setCep("11740-000");
		endereco.setCidade("São Paulo");
		endereco.setLogradouro("Rua Clélia");
		endereco.setNumero("1243");

		funcionario = new Funcionario(ID, NOME, CPF, DATA_NASCIMENTO, TELEFONE, endereco, SALARIO);
		
		funcionarioDTO = new FuncionarioDTO();
		funcionarioDTO.setCpf(CPF);
		funcionarioDTO.setId(ID);
		funcionarioDTO.setDataNascimento(DATA_NASCIMENTO);
		funcionarioDTO.setNome(NOME);
		funcionarioDTO.setSalario(SALARIO);
		
		funcionarioResumoDTO = new FuncionarioResumoDTO();
		funcionarioResumoDTO.setCpf(CPF);
		funcionarioResumoDTO.setDataNascimento(DATA_NASCIMENTO);
		funcionarioResumoDTO.setNome(NOME);
		funcionarioResumoDTO.setSalario(SALARIO);
		funcionarioResumoDTO.setId(ID);
		
		funcionarioInputDTO = new FuncionarioInputDTO();
		funcionarioInputDTO.setCpf(CPF);
		funcionarioInputDTO.setNome(NOME);
		funcionarioInputDTO.setSalario(SALARIO);
		
		dto = new NovoSalarioDTO();
		dto.setCpf(CPF);
		dto.setNovoSalario(new BigDecimal(460.00).setScale(2, RoundingMode.HALF_EVEN));
		dto.setPercentual("15%");
		dto.setReajuste(new BigDecimal(60.00).setScale(2, RoundingMode.HALF_EVEN));
		
		impostoDTO = new ImpostoDTO();
		impostoDTO.setCpf(CPF);
		impostoDTO.setImposto("Isento");

	}

}
