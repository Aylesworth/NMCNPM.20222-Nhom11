package com.aylesw.mch.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	@Fetch(value = FetchMode.SELECT)
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	private Set<User> users;
}
