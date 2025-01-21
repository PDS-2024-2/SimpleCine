package br.ufrn.imd.cine.services.combo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.imd.cine.model.combo.ComboRecord;
import br.ufrn.imd.cineframework.models.combo.Combo;
import br.ufrn.imd.cineframework.repositories.GenericRepository;
import br.ufrn.imd.cineframework.repositories.combo.ComboRepository;
import br.ufrn.imd.cineframework.services.GenericService;

@Service
public class ComboService extends GenericService<Combo> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private ComboRepository comboRepository;

	@Override
	public GenericRepository<Combo> getRepository() {
		return comboRepository;
	}

	public Combo createCombo(ComboRecord comboRecord) {
		Combo combo = new Combo();
		combo.setTitle(comboRecord.title());
		combo.setDescription(comboRecord.description());
		combo.setItems(comboRecord.items());

		return insert(combo);
	}

	public Combo updateCombo(Long id, ComboRecord comboRecord) {

		Combo existingCombo = comboRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Combo not found with id: " + id));

		existingCombo.setTitle(comboRecord.title());
		existingCombo.setDescription(comboRecord.description());
		existingCombo.setItems(comboRecord.items());

		return update(existingCombo);
	}

}
