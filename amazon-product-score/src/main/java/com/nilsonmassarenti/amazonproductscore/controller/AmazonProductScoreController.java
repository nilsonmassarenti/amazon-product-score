package com.nilsonmassarenti.amazonproductscore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nilsonmassarenti.amazonproductscore.dto.ScoreDTO;
import com.nilsonmassarenti.amazonproductscore.service.AmazonProductScoreService;

/**
 * This Class is responsible for receive HTTP requests to calculate the product scores
 * 
 * @author Nilson Massarenti
 *
 */
@RestController
public class AmazonProductScoreController {
	
	/**
	 * The service class to calculate the amazon product score
	 */
	@Autowired
	private AmazonProductScoreService service;

	/**
	 * This method is responsible for receive the GET requests to calculate score
	 * 
	 * @param keyword the product name to check score
	 * @return the score of product
	 * @since 1.0
	 */
	@GetMapping("/estimate")
	public ResponseEntity<?> getEstimateScore(@RequestParam(value="keyword", required=true) String keyword){
		
		Integer score = service.executeScoreCalculations(keyword);
		if (score > -1) {
			ScoreDTO scoreDTO = new ScoreDTO(keyword, score);
			return new ResponseEntity<ScoreDTO>(scoreDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		
	}
}
