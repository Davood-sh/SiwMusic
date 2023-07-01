package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Musician;
import it.uniroma3.siw.repository.MusicianRepository;

@Component
public class MusicianValidator implements Validator{
	
	@Autowired
	private MusicianRepository musicianRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Musician musician = (Musician)o;
		if(musician.getName()!=null && musician.getSurname()!=null 
				&& this.musicianRepository.existsByNameAndSurname(musician.getName(), musician.getSurname())) {
			errors.reject("musician.duplicate");
		}
		
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Musician.class.equals(aClass);
	}


}
