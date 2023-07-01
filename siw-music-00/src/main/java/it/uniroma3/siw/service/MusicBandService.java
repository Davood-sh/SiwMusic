package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.MusicBand;
import it.uniroma3.siw.repository.MusicBandRepository;
import jakarta.validation.Valid;

@Service
public class MusicBandService {
	@Autowired
	private MusicBandRepository musicBandRepository;

	@Transactional
	public void save(MusicBand musicBand) {
		this.musicBandRepository.save(musicBand);		
	}
	
	public List<MusicBand> findAll(){
		List<MusicBand> result = new ArrayList<>();
		Iterable<MusicBand> iterable = this.musicBandRepository.findAll();
		for(MusicBand musicBAnd : iterable)
			result.add(musicBAnd);
		return result;
	}

	public MusicBand findByNameBand(String nameBand) {
		return this.musicBandRepository.findByNameBand(nameBand);
	}

	public MusicBand findById(Long id) {
		return this.musicBandRepository.findById(id).get();
	}

}
