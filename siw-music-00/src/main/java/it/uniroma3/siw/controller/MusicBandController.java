package it.uniroma3.siw.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.MusicBandValidator;
import it.uniroma3.siw.model.MusicBand;
import it.uniroma3.siw.model.Musician;
import it.uniroma3.siw.repository.MusicBandRepository;
import it.uniroma3.siw.repository.MusicianRepository;
import it.uniroma3.siw.service.MusicBandService;
import it.uniroma3.siw.service.MusicianService;
import jakarta.validation.Valid;

@Controller
public class MusicBandController {
	
	@Autowired
	private MusicBandRepository musicBandRepository;
	
	@Autowired
	private MusicianRepository musicianRepository;
	
	@Autowired
	private MusicBandValidator musicBandValidator;
	
	@Autowired
	private MusicBandService musicBandService;
	
	@Autowired
	private MusicianService musicianService;
	
	@GetMapping(value = "/admin/formNewMusicBand")
	public String formNewMusicBand(Model model) {
		model.addAttribute("musicBand", new MusicBand());
		return "admin/formNewMusicBand.html";
	}
	
	@PostMapping(value = "/admin/musicBand")
	public String newMusicBand(@Valid @ModelAttribute("musicBand") MusicBand musicBand ,BindingResult bindingResult,
			@RequestParam("fileImage") MultipartFile multipartFile,Model model) throws IOException {
		
		this.musicBandValidator.validate(musicBand, bindingResult);
		if(!bindingResult.hasErrors()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			musicBand.setUrlImage(fileName);
			
		this.musicBandService.save(musicBand);
		model.addAttribute("musicBand", musicBand);
		
		String uploadDir = "./photos/" + musicBand.getId();
		Path uploadPath = Paths.get(uploadDir);

		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try( InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(fileName);
			System.out.println(filePath.toFile().getAbsolutePath());
			Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e) {
			throw new IOException("could not save Uploaded file" + fileName);
		}

		return "musicBand.html";
		}else {
			return "admin/formNewMusicBand.html";
		}
	}
	
	@GetMapping(value = "/musicBand")
	public String getMusicBands(Model model) {
		model.addAttribute("musicBands", this.musicBandService.findAll());
		return "musicBands.html";
	}
	
	@GetMapping(value = "/admin/indexMusicBand")
	public String indexMusicBand() {
		return "admin/indexMusicBand.html";
	}
	
	@GetMapping(value = "/formSearchMusicBand")
	public String formSearchMusicBand() {
		return "formSearchMusicBand.html";
	}
	
	@PostMapping(value = "/searchMusicBand")
	public String searchMusicBand(Model model ,@RequestParam String nameBand) {
		model.addAttribute("musicBand",this.musicBandService.findByNameBand(nameBand));
		return "foundMusicBand.html";
	}
	
	@GetMapping(value = "/musicBand/{id}")
	public String getMusicBand(@PathVariable("id") Long id,Model model) {
		model.addAttribute("musicBand", this.musicBandService.findById(id));
		return "musicBand.html";
	}
	
	@GetMapping(value = "/admin/manageMusicBands")
	public String manageMusicBand(Model model) {
		model.addAttribute("musicBands", this.musicBandService.findAll());
		return "admin/manageMusicBands.html";
	}
	
	@GetMapping(value = "/admin/formUpdateMusicBand/{id}")
	public String formUpdateMusicBand(@PathVariable("id") Long id , Model model) {
		model.addAttribute("musicBand", this.musicBandService.findById(id));
		return "admin/formUpdateMusicBand.html";
	}
	
	@GetMapping(value = "/admin/updateMusicians/{idmb}")
	public String updateMusicians(@PathVariable("idmb") Long idmb , Model model) {
		MusicBand musicBand = this.musicBandService.findById(idmb);
		List<Musician> musiciansToAdd = this.musiciansToAdd(idmb);
		
		model.addAttribute("musicBand", musicBand);
		model.addAttribute("musiciansToAdd", musiciansToAdd);		
		
		return "admin/musiciansToAdd.html";
	}
	
	private List<Musician> musiciansToAdd(Long musicBandId) {
		List<Musician> musiciansToAdd = new ArrayList<>();

		for (Musician m : this.musicianRepository.findMusiciansNotInMusicBand(musicBandId)) {
			musiciansToAdd.add(m);
		}
		return musiciansToAdd;
	}
	
	@GetMapping(value = "/admin/removeMusicianFromMusicBand/{mid}/{mbid}")
	public String removeMusicianfromMusicBand(@PathVariable("mid") Long mid,@PathVariable("mbid") Long mbid , Model model) {
		MusicBand musicBand = this.musicBandService.findById(mbid);
		Musician musician = this.musicianService.findById(mid);
		musicBand.getMusicians().remove(musician);
		musician.setMusicBand(null);
		this.musicBandService.save(musicBand);
		this.musicianService.save(musician);
		List<Musician> musiciansToAdd = this.musiciansToAdd(mbid);
		model.addAttribute("musiciansToAdd", musiciansToAdd);
		model.addAttribute("musicBand", musicBand);
		return "admin/musiciansToAdd.html";
	}
	
	@GetMapping(value = "/admin/addMusicianToMusicBand/{mid}/{mbid}")
	public String addMusicianToMusicBand(@PathVariable("mid") Long mid,@PathVariable("mbid") Long mbid , Model model) {
		MusicBand musicBand = this.musicBandService.findById(mbid);
		Musician musician = this.musicianService.findById(mid);
		musicBand.getMusicians().add(musician);
		musician.setMusicBand(musicBand);
		this.musicianService.save(musician);
		this.musicBandService.save(musicBand);
		List<Musician> musiciansToAdd = this.musiciansToAdd(mbid);
		model.addAttribute("musiciansToAdd", musiciansToAdd);
		model.addAttribute("musicBand", musicBand);
		return "admin/musiciansToAdd.html";
	}
	

}
