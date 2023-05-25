package mch.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "body_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodyMetrics {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "child_id")
	@JsonIgnore
	private Child child;
	
	@NotNull
	@Min(value = 0)
	private Double height;
	
	@NotNull
	@Min(value = 0)
	private Double weight;
	
	@NotNull
	private Date measurementDate;
	
	private String note;
}
