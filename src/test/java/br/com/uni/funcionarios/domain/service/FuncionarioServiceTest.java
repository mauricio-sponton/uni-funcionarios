package br.com.uni.funcionarios.domain.service;

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
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import br.com.uni.funcionarios.api.dto.output.ImpostoDTO;
import br.com.uni.funcionarios.api.dto.output.NovoSalarioDTO;
import br.com.uni.funcionarios.domain.exception.FuncionarioNaoEncontradoException;
import br.com.uni.funcionarios.domain.exception.NegocioException;
import br.com.uni.funcionarios.domain.model.Endereco;
import br.com.uni.funcionarios.domain.model.Funcionario;
import br.com.uni.funcionarios.domain.repository.FuncionarioRepository;

@SpringBootTest
class FuncionarioServiceTest {

	private static final BigDecimal SALARIO = new BigDecimal(400);

	private static final String TELEFONE = "13991655284";

	private static final LocalDate DATA_NASCIMENTO = LocalDate.now();

	private static final String CPF = "200.584.322-99";

	private static final String NOME = "Luiz";

	private static final long ID = 1L;

	@InjectMocks
	private FuncionarioService service;

	@Mock
	private FuncionarioRepository repository;

	private Optional<Funcionario> optionalFuncionario;
	private Funcionario funcionario;
	private Endereco endereco;
	private NovoSalarioDTO dto;
	private ImpostoDTO impostoDTO;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		startFuncionario();
	}

	@Test
	void whenFindAll_thenReturnListFuncionarios() {
		when(repository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(List.of(funcionario));

		List<Funcionario> resposta = service.listar();

		assertNotNull(resposta);
		assertEquals(1, resposta.size());
		assertEquals(Funcionario.class, resposta.get(0).getClass());
		assertEquals(ID, resposta.get(0).getId());
		assertEquals(NOME, resposta.get(0).getNome());
		assertEquals(CPF, resposta.get(0).getCpf());
		assertEquals(SALARIO, resposta.get(0).getSalario());
	}

	@Test
	void whenCreate_thenReturnSuccess() {
		when(repository.save(any())).thenReturn(funcionario);

		Funcionario resposta = service.salvar(funcionario);

		assertNotNull(resposta);
		assertEquals(Funcionario.class, resposta.getClass());
		assertEquals(ID, resposta.getId());
		assertEquals(NOME, resposta.getNome());
		assertEquals(CPF, resposta.getCpf());
		assertEquals(DATA_NASCIMENTO, resposta.getDataNascimento());
		assertEquals(TELEFONE, resposta.getTelefone());
		assertEquals(SALARIO, resposta.getSalario());
	}

	@Test
	void whenCreate_thenReturnCpfJaCadastrado() {
		when(repository.findByCpf(anyString())).thenReturn(optionalFuncionario);

		try {
			optionalFuncionario.get().setId(2L);
			service.salvar(funcionario);
		} catch (Exception e) {
			assertEquals(NegocioException.class, e.getClass());
			assertEquals("Já existe um funcionário cadastrado com o CPF: " + CPF, e.getMessage());
		}

	}

	@Test
	void whenFindById_thenReturnInstanceFuncionario() {
		when(repository.findById(anyLong())).thenReturn(optionalFuncionario);
		Funcionario resposta = service.buscarOuFalhar(ID);

		assertNotNull(resposta);
		assertEquals(Funcionario.class, resposta.getClass());
		assertEquals(ID, resposta.getId());
		assertEquals(NOME, resposta.getNome());
		assertEquals(CPF, resposta.getCpf());
		assertEquals(DATA_NASCIMENTO, resposta.getDataNascimento());
		assertEquals(TELEFONE, resposta.getTelefone());
		assertEquals(SALARIO, resposta.getSalario());
	}

	@Test
	void whenFindById_thenReturnFuncionarioNaoEncontradoException() {
		when(repository.findById(anyLong())).thenThrow(new FuncionarioNaoEncontradoException(ID));

		try {
			service.buscarOuFalhar(ID);
		} catch (Exception e) {
			assertEquals(FuncionarioNaoEncontradoException.class, e.getClass());
			assertEquals("Não existe um cadastro de funcionário com código " + ID, e.getMessage());
		}
	}

	@Test
	void excluirWithSuccess() {
		when(repository.findById(anyLong())).thenReturn(optionalFuncionario);
		doNothing().when(repository).deleteById(anyLong());
		service.excluir(ID);
		verify(repository, times(1)).deleteById(anyLong());
	}

	@Test
	void testCalcularNovoSalario() {
		when(repository.findByCpf(anyString())).thenReturn(optionalFuncionario);
		NovoSalarioDTO resposta = service.calcularNovoSalario(CPF);
		assertNotNull(resposta);
		assertEquals(NovoSalarioDTO.class, resposta.getClass());
		assertEquals(dto.getCpf(), resposta.getCpf());
		assertEquals(dto.getNovoSalario(), resposta.getNovoSalario());
		assertEquals(dto.getReajuste(), resposta.getReajuste());
		assertEquals(dto.getPercentual(), resposta.getPercentual());
	}

	@Test
	void testCalcularImposto() {
	
		when(repository.findByCpf(anyString())).thenReturn(optionalFuncionario);
		ImpostoDTO resposta = service.calcularImposto(CPF);
		assertNotNull(resposta);
		assertEquals(ImpostoDTO.class, resposta.getClass());
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
		optionalFuncionario = Optional.of(new Funcionario(ID, NOME, CPF, DATA_NASCIMENTO, TELEFONE, endereco, SALARIO));
		
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
