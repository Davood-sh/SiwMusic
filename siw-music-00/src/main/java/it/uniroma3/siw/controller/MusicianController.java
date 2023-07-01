package it.uniroma3.siw.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

import it.uniroma3.siw.controller.validator.MusicianValidator;
import it.uniroma3.siw.model.Musician;
import it.uniroma3.siw.repository.MusicianRepository;
import it.uniroma3.siw.service.MusicianService;
import jakarta.validation.Valid;

@Controller
public class MusicianController {

	@Autowired
	private MusicianRepository musicianRepository;

	@Autowired
	private MusicianValidator musicianValidator;
	
	@Autowired
	private MusicianService musicianService;

	@GetMapping(value = "/admin/formNewMusician")
	public String formNewMusician(Model model) {
		model.addAttribute("musician", new Musician());
		return "admin/formNewMusician.html";
	}

	@PostMapping(value = "/admin/musician")
	public String newMusician(@Valid @ModelAttribute("musician") Musician musician,BindingResult bindingResult,
			@RequestParam("fileImage") MultipartFile multipartFile,Model model) throws IOException {

		this.musicianValidator.validate(musician, bindingResult);
		if(!bindingResult.hasErrors()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			musician.setUrlImage(fileName);

			this.musicianService.save(musician);
			model.addAttribute("musician", musician);

			String uploadDir = "./photos/" + musician.getId();
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

			return "musician.html";
		} else {
			return "admin/formNewMusician.html";
		}

	}

	@GetMapping(value = "/musician")
	public String getMusicians(Model model) {
		model.addAttribute("musicians", this.musicianService.findAll());
		return "musicians.html";
	}
	
	@GetMapping(value = "admin/indexMusician")
	public String indexMusician() {
		return "admin/indexMusician.html";
	}
	
	@GetMapping(value = "/musician/{id}")
	public String getMusicBand(@PathVariable("id") Long id,Model model) {
		model.addAttribute("musician", this.musicianService.findById(id));
		return "musician.html";
	}

}
