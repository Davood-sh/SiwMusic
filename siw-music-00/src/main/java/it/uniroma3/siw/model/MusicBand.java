package it.uniroma3.siw.model;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class MusicBand {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank
	private String nameBand;
	
	private String urlImage;
	@NotNull
	private Integer formed;
	
	private String country;
	
	@OneToMany(mappedBy ="musicBand")
	private Set<Musician> musicians;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameBand() {
		return nameBand;
	}

	public void setNameBand(String nameBand) {
		this.nameBand = nameBand;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public Integer getFormed() {
		return formed;
	}

	public void setFormed(Integer formed) {
		this.formed = formed;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Set<Musician> getMusicians() {
		return musicians;
	}

	public void setMusicians(Set<Musician> musicians) {
		this.musicians = musicians;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nameBand);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MusicBand other = (MusicBand) obj;
		return Objects.equals(nameBand, other.nameBand);
	}
	
	

}
