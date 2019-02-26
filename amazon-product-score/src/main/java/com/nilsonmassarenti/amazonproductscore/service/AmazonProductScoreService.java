package com.nilsonmassarenti.amazonproductscore.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nilsonmassarenti.amazonproductscore.integration.AmazonCompletionIntegration;

/**
 * This Class is responsible for the service to calculate the product score
 * 
 * @author Nilson Massarenti
 *
 */
@Service
public class AmazonProductScoreService {

	@Autowired
	private AmazonCompletionIntegration integration;

	public Integer executeScoreCalculations(String keyword) {

		Integer score = 0;
		Callable<Integer> task = () -> {
			return calculateScore(keyword);
		};
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<Integer> future = executor.submit(task);
		try {
			score = future.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			return -1;
		}
		return score;
	}

	private Integer calculateScore(String keyword) {
		String keywordToSearch = keyword.replace(" ", "+");
		Integer factorScore = 0;
		Integer factorScoreFound = 0;
		Integer keywordSize = keyword.length();
		for (int i = 0; i < keywordToSearch.length(); i++) {
			String integrationResult = integration.getResults(keyword.replace(" ", "+").substring(0, i + 1));
			factorScore += (keywordSize * 100);
			if (integrationResult != null || integrationResult != "") {
				if (searchKeyword(integrationResult, keyword) == true) {
					factorScoreFound = (keywordSize * 100);
					break;
				}
			}
			keywordSize--;
		}
		Integer score = 0;
		if (factorScoreFound > 0) {
			score = (factorScoreFound * 100) / factorScore;
		}
		return score;
	}

	private Boolean searchKeyword(String integrationResult, String keyword) {
		Boolean foundKeyword = false;
		JsonElement resultElement = new JsonParser().parse(integrationResult);
		JsonArray results = (JsonArray) resultElement.getAsJsonArray().get(1);

		for (JsonElement result : results) {
			if (result.getAsString().equals(keyword)) {
				foundKeyword = true;
				break;
			}
		}
		return foundKeyword;
	}

}
