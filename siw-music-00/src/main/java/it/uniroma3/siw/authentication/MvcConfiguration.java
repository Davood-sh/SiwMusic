package it.uniroma3.siw.authentication;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		Path artistImageUploadDir = Paths.get("./photos/");
		String artistImageUploadPath = artistImageUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/photos/**").addResourceLocations("file:/" + artistImageUploadPath +"/");
		
		
	}
}