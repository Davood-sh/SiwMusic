package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.MusicBand;
import it.uniroma3.siw.repository.MusicBandRepository;

@Component
public class MusicBandValidator implements Validator {

	@Autowired
	private MusicBandRepository musicBandRepository;

	@Override
	public void validate(Object o, Errors errors) {
		MusicBand musicBand = (MusicBand)o;
		if(musicBand.getNameBand()!=null && this.musicBandRepository.existsByNameBand(musicBand.getNameBand())) {
			errors.reject("musicBand.duplicate");
		}		
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return MusicBand.class.equals(aClass);
	}


}
