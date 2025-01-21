package br.ufrn.imd.cine.services.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.imd.cine.model.layout.SimpleLayout;
import br.ufrn.imd.cineframework.services.layout.LayoutService;

@Service
public class SimpleLayoutService {

	@Autowired
	private SimpleLayoutCreator simpleLayoutCreator;

	public SimpleLayout createLayout(Object data) {
		LayoutService layoutService = new LayoutService(simpleLayoutCreator);
		SimpleLayout simpleLayout = (SimpleLayout) layoutService.createLayout(data);
		return simpleLayout;
	}
}
