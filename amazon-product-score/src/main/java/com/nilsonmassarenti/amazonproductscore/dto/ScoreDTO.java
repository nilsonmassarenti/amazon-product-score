package com.nilsonmassarenti.amazonproductscore.dto;

public class ScoreDTO {
	
	public ScoreDTO(String keyword, Integer score) {
		this.keyword = keyword;
		this.score = score;
	}
	
	private String keyword;
	private Integer score;
	
	public String getKeyword() {
		return keyword;
	}
	public Integer getScore() {
		return score;
	}
	
	
	
}
