package br.ufrn.imd.cine.services.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.imd.cine.model.ai.PromptMovieRecomendationRecord;
import br.ufrn.imd.cineframework.services.ai.AIService;

@Service
public class SimpleAIService {

	@Autowired
	private ChatGPT chatGPT;

	private AiSimpleProcess aiSimpleProcess = new AiSimpleProcess(chatGPT);

	private AIService aiService = new AIService(aiSimpleProcess);

	public String analysis(PromptMovieRecomendationRecord r) {
		return aiService.analysis(r);
	}
}
