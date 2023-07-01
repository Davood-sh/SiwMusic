package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.MusicBand;
import it.uniroma3.siw.model.Musician;
import it.uniroma3.siw.repository.MusicianRepository;

@Service
public class MusicianService {
	@Autowired
	private MusicianRepository musicianRepository;

	public Musician findById(Long mid) {
		return this.musicianRepository.findById(mid).get();
	}
	
	@Transactional
	public void save(Musician musician) {
		this.musicianRepository.save(musician);		
	}
	
	public List<Musician> findAll(){
		List<Musician> result = new ArrayList<>();
		Iterable<Musician> iterable = this.musicianRepository.findAll();
		for(Musician musician : iterable)
			result.add(musician);
		return result;
	}

}
