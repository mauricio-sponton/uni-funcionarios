package br.com.uni.funcionarios.api.exceptionhandler;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class Problem {

	private Integer status;
	private String type;
	private String title;
	private String detail;
	private String userMessage;
	private LocalDate timestamp;
	private List<Field> fields;

	public Integer getStatus() {
		return status;
	}

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getDetail() {
		return detail;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public LocalDate getTimestamp() {
		return timestamp;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public static class Field {
		private String name;
		private String userMessage;

		public String getName() {
			return name;
		}

		public String getUserMessage() {
			return userMessage;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setUserMessage(String userMessage) {
			this.userMessage = userMessage;
		}
		
		

	}

}
