package mch.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "examination")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Examination {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "child_id")
	@JsonIgnore
	private Child child;
	
	@NotNull
	private Date date;
	
	private String facility;
	
	private String reason;
	
	private String result;
	
	private String advice;
	
	private String note;
}
