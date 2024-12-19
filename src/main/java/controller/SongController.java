package controller;

import model.Song;
import model.SongForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.ISongService;
import service.SongService;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/song")
public class SongController {
	@Autowired
	private ISongService songService;

	@GetMapping
	public String showAllSongs(Model model) {
		model.addAttribute("songs", songService.findAll());
		return "list";
	}

	@GetMapping("/create")
	public String showForm(Model model) {
		model.addAttribute("songForm", new SongForm());
		return "create";
	}

	@Value("${file-upload}")
	private String folderPath;

	@PostMapping("/save")
	public String saveSong(SongForm songForm, RedirectAttributes redirectAttributes) {
		MultipartFile multipartFile = songForm.getFile();
		String fileName = multipartFile.getOriginalFilename();
		if (!rightFile(fileName)) {
			redirectAttributes.addFlashAttribute("message", "File is not a valid file! Only accept: .mp3, .wav, .ogg, .m4p");
			return "redirect:/song/create";
		}
		try {
			FileCopyUtils.copy(songForm.getFile().getBytes(), new File(folderPath + fileName));
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		int id;
		do {
			id = (int) (Math.random() * 10000);
		} while (songService.findById(id) != null);
		Song song = new Song(id, songForm.getTitle(), songForm.getArtist(),
				songForm.getGender(), fileName);
		songService.add(song);

		redirectAttributes.addFlashAttribute("message", "Add new song successfully!");
		return "redirect:/song";
	}

	private boolean rightFile(String fileName) {
		String[] acceptedExtensions = {".mp3", ".wav", ".ogg", ".m4p"};
		String extension = fileName.substring(fileName.lastIndexOf("."));
		boolean accepted = false;
		for (String ext : acceptedExtensions) {
			if (extension.equals(ext)) {
				accepted = true;
			}
		}
		return accepted;
	}
}
