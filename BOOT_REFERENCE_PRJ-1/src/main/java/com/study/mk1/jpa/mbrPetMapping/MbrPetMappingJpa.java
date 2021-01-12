package com.study.mk1.jpa.mbrPetMapping;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;
import com.study.mk1.jpa.mbrPrjMapping.MbrPrjMappingJpa;
import com.study.mk1.jpa.pet.PetJpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mbr_pet_mapping")
@AllArgsConstructor
@NoArgsConstructor
@Data
@IdClass(MbrPetMappingId.class)
public class MbrPetMappingJpa extends AbstractEntity{
	
	@Id
	@Column(name = "mbr_seq")
	long mbrSeq;
	
	@Id
	@Column(name = "pet_seq")
	long petSeq;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "mbr_seq" , updatable = false, insertable = false)
	MbrJpa mbrJpa;
	
	@OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	@JoinColumn(name="pet_seq",updatable = false, insertable = false )
	PetJpa petJpa;
}
