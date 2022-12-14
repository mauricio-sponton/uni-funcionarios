package br.com.uni.funcionarios.api.exceptionhandler;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import br.com.uni.funcionarios.domain.exception.EntidadeNaoEncontradaException;
import br.com.uni.funcionarios.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
			+ "o problema persistir, entre em contato com o administrador do sistema.";

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos est??o inv??lidos. Fa??a o preenchimento correto e tente novamente.";

		BindingResult bindingResult = ex.getBindingResult();

		List<Problem.Field> problemFields = bindingResult.getFieldErrors().stream().map(fieldError -> {
			String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());

			Problem.Field campos = new Problem.Field();
			campos.setName(fieldError.getField());
			campos.setUserMessage(message);

			return campos;
		}).collect(Collectors.toList());

		Problem problem = createProblemBuilder(status, problemType, detail);
		problem.setUserMessage(detail);
		problem.setFields(problemFields);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

		ex.printStackTrace();

		Problem problem = createProblemBuilder(status, problemType, detail);
		problem.setUserMessage(detail);

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = String.format("O recurso %s, que voc?? tentou acessar, ?? inexistente.", ex.getRequestURL());

		Problem problem = createProblemBuilder(status, problemType, detail);
		problem.setUserMessage(MSG_ERRO_GENERICA_USUARIO_FINAL);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

		String detail = String.format(
				"O par??metro de URL '%s' recebeu o valor '%s', "
						+ "que ?? de um tipo inv??lido. Corrija e informe um valor compat??vel com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Problem problem = createProblemBuilder(status, problemType, detail);
		problem.setUserMessage(MSG_ERRO_GENERICA_USUARIO_FINAL);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
		}

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisi????o est?? inv??lido. Verifique erro de sintaxe.";

		Problem problem = createProblemBuilder(status, problemType, detail);
		problem.setUserMessage(MSG_ERRO_GENERICA_USUARIO_FINAL);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String path = joinPath(ex.getPath());

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' n??o existe na Entidade '%s'. "
						+ "Corrija ou remova essa propriedade e tente novamente.",
				path, ex.getReferringClass().getSimpleName());

		Problem problem = createProblemBuilder(status, problemType, detail);
		problem.setUserMessage(MSG_ERRO_GENERICA_USUARIO_FINAL);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' recebeu o valor '%s', "
						+ "que ?? de um tipo inv??lido. Corrija e informe um valor compat??vel com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		Problem problem = createProblemBuilder(status, problemType, detail);
		problem.setUserMessage(MSG_ERRO_GENERICA_USUARIO_FINAL);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex,
			WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail);
		problem.setUserMessage(detail);

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail);
		problem.setUserMessage(detail);

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {

			Problem problem = new Problem();
			problem.setTimestamp(LocalDate.now());
			problem.setStatus(status.value());
			problem.setTitle(status.getReasonPhrase());
			problem.setUserMessage(MSG_ERRO_GENERICA_USUARIO_FINAL);

			body = problem;

		} else if (body instanceof String) {

			Problem problem = new Problem();
			problem.setTimestamp(LocalDate.now());
			problem.setStatus(status.value());
			problem.setTitle((String) body);
			problem.setUserMessage(MSG_ERRO_GENERICA_USUARIO_FINAL);

			body = problem;
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private Problem createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {

		Problem problem = new Problem();
		problem.setTimestamp(LocalDate.now());
		problem.setStatus(status.value());
		problem.setType(problemType.getUri());
		problem.setTitle(problemType.getTitle());
		problem.setDetail(detail);

		return problem;

	}

	private String joinPath(List<Reference> references) {
		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}
}
