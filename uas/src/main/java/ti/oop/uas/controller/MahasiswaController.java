package ti.oop.uas.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ti.oop.uas.model.Mahasiswa;
import ti.oop.uas.service.MahasiswaService;

@Controller
public class MahasiswaController {
	@Autowired
	private MahasiswaService mahasiswaService;
	@GetMapping("/")
	public String viewHomePage(Model model) {
		return findPaginated(1, "namaLengkap", "asc", model);		
	}
	@GetMapping("/TambahMahasiswa")
	public String TambahMahasiswa(Model model) {
		Mahasiswa mahasiswa = new Mahasiswa();
		model.addAttribute("mahasiswa", mahasiswa);
		return "tambahMahasiswa";
	}
	@PostMapping("/saveMahasiswa")
	public String saveMahasiswa(@ModelAttribute("mahasiswa") Mahasiswa mahasiswa) {
		mahasiswaService.saveMahasiswa(mahasiswa);
		return "redirect:/";
	}
	@GetMapping("/UpdateMahasiswa/{id}")
	public String UpdateMahasiswa(@PathVariable ( value = "id") long id, Model model) {
		Mahasiswa mahasiswa = mahasiswaService.getMahasiswaById(id);
		model.addAttribute("mahasiswa", mahasiswa);
		return "updateMahasiswa";
	}
	@GetMapping("/deleteMahasiswa/{id}")
	public String deleteMahasiswa(@PathVariable (value = "id") long id) {
		this.mahasiswaService.deleteMahasiswaById(id);
		return "redirect:/";
	}
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 5;
		Page<Mahasiswa> page = mahasiswaService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Mahasiswa> listMahasiswa = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("listMahasiswa", listMahasiswa);
		return "index";
	}
}