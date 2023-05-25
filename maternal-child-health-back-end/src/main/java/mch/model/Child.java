package mch.model;

import java.sql.Date;

import org.hibernate.annotations.DialectOverride.Formula;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "child")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Child {
	@Id
	@GeneratedValue
	private Long id;
	
	private String fullName;
	
	private String nickname;
	
	private Date dob;
	
	private String sex;
	
	private String ethnicity;
	
	private String nationality;
	
	private String birthplace;
	
	private String insuranceId;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private User parent;
}
