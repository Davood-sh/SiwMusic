package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.MusicBand;

public interface MusicBandRepository extends CrudRepository<MusicBand, Long>{
	
	public boolean existsByNameBand(String nameBand);
	
	public MusicBand findByNameBand(String nameBand);
	
	
	
	

}
