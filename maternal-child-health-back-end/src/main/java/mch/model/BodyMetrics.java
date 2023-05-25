package mch.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "body_metrics")
public class BodyMetrics {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "child_id")
	private Child child;
	
	private Double height;
	
	private Double weight;
	
	private Date measurementDate;
	
	private String note;
}
