package org.jsp.merchantbootapp.controller;

import java.util.List;
import java.util.Optional;

import org.jsp.merchantbootapp.dto.Merchant;
import org.jsp.merchantbootapp.dto.ResponseStructure;
import org.jsp.merchantbootapp.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {
	@Autowired
	private MerchantRepository merchantRepository;

	@PostMapping
	public ResponseStructure<Merchant> saveMerchant(@RequestBody Merchant merchant) {
		ResponseStructure<Merchant> structure = new ResponseStructure<>();
		structure.setMessage("Merchant saved successfullY");
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setData(merchantRepository.save(merchant));
		return structure;
	}

	@GetMapping("/{id}")
	public ResponseStructure<Merchant> findById(@PathVariable(name = "id") int id) {
		ResponseStructure<Merchant> structure = new ResponseStructure<>();
		Optional<Merchant> recMerchant = merchantRepository.findById(id);
		if (recMerchant.isPresent()) {
			structure.setMessage("Merchant found");
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setData(recMerchant.get());
			return structure;

		}
		structure.setData(null);
		structure.setMessage("Merchant not found as id as INVALID");
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return structure;
	}

	@PutMapping
	public ResponseStructure<Merchant> updateMerchant(@RequestBody Merchant merchant) {
		ResponseStructure<Merchant> structure = new ResponseStructure<>();
		Optional<Merchant> recMerchant = merchantRepository.findById(merchant.getId());
		if (recMerchant.isPresent()) {
			Merchant dbMerchant = recMerchant.get();
			dbMerchant.setEmail(merchant.getEmail());
			dbMerchant.setGst_number(merchant.getGst_number());
			dbMerchant.setName(merchant.getName());
			dbMerchant.setPassword(merchant.getPassword());
			dbMerchant.setPhone(merchant.getPhone());
			merchantRepository.save(dbMerchant);
			structure.setMessage("Merchant updated with id" + merchant.getId());
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			structure.setData(dbMerchant);
			return structure;
		}
		structure.setMessage("Merchant not update as Id is Invalid");
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setData(null);
		return structure;
	}

	@GetMapping
	public List<Merchant> findAll() {
		return merchantRepository.findAll();
	}

	@DeleteMapping("/{id}")
	public ResponseStructure<Merchant> deleteMerchant(@PathVariable(name = "id") int id) {
		ResponseStructure<Merchant> structure = new ResponseStructure<>();
		Optional<Merchant> recMerchant = merchantRepository.findById(id);

		if (recMerchant.isPresent()) {
			merchantRepository.delete(recMerchant.get());
			structure.setMessage("merchant deleted with id" + id);
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setData(null);
			return structure;
		}
		structure.setMessage("cannot delete Merchant as id is Invalid");
		structure.setData(null);
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return structure;
	}

	@GetMapping("/find-by-name/{name}")
	public ResponseStructure<List<Merchant>> findByName(@PathVariable(name = "name") String name) {
		ResponseStructure<List<Merchant>> structure = new ResponseStructure<>();
		List<Merchant> merchants = merchantRepository.findByName(name);
		structure.setData(merchants);
		if (merchants.isEmpty()) {
			structure.setMessage("No merchant found with entered name");
			structure.setStatusCode(HttpStatus.NO_CONTENT.value());
			return structure;
		}
		structure.setMessage("List of Merchants with entered name:");
		structure.setStatusCode(HttpStatus.OK.value());
		return structure;
	}

	@GetMapping("find-by-phone/{phone}")
	public ResponseStructure<Merchant> findByPhone(@PathVariable(name = "phone") long phone) {
		ResponseStructure<Merchant> structure = new ResponseStructure<>();
		Optional<Merchant> recMerchant = merchantRepository.findByPhone(phone);
		structure.setData(recMerchant.get());
		if (recMerchant.isPresent()) {
			structure.setMessage("Merchant found with entered phone");
			structure.setStatusCode(HttpStatus.OK.value());
			return structure;
		}
//		if (recMerchant.isPresent())
//			return recMerchant.get();
		// return null;
		structure.setMessage("Merchant not found with entered phone");
		structure.setData(null);
		structure.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return structure;
	}

	@PostMapping("/verify-by-email")
	public ResponseStructure<Merchant> verify(@RequestParam(name = "email") String email,
			@RequestParam(name = "password") String password) {
		ResponseStructure<Merchant> structure = new ResponseStructure<>();
		Optional<Merchant> recMerchant = merchantRepository.verifyByEmailAndPasword(email, password);
		structure.setData(recMerchant.get());
		if (recMerchant.isPresent()) {
			structure.setMessage("Merchant found with entered email and password");
			structure.setStatusCode(HttpStatus.OK.value());
			return structure;
		}
		structure.setMessage("Merchand not found with entered email and password");
		structure.setData(null);
		structure.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return structure;
	}
}
