package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.MusicBand;
import it.uniroma3.siw.model.Musician;

public interface MusicianRepository extends CrudRepository<Musician, Long>{
	
	public boolean existsByNameAndSurname(String name,String surname);
	
	
	public List<Musician> findAllMusiciansByMusicBand(MusicBand musicBand);	
	
	@Query(value="select * "
			+ "from musician m "
			+ "where m.id not in "
			+ "(select id "
			+ "from musician "
			+ "where musician.music_band_id = :musicBandId)", nativeQuery=true)
	public Iterable<Musician> findMusiciansNotInMusicBand(@Param("musicBandId") Long id);
	
	
	

}
