package com.nilsonmassarenti.amazonproductscore.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Component;

/**
 * This Class is responsible for the integration with amazon search
 * 
 * @author Nilson Massarenti
 *
 */
@Component
public class AmazonCompletionIntegration {

	public String getResults(String keyword) {
		String bodyResponse = "";
		try {
			URL url = new URL(
					"http://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q="
							+ keyword);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				bodyResponse += output;
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			bodyResponse = null;
		} catch (IOException e) {
			e.printStackTrace();
			bodyResponse = null;
		}
		return bodyResponse;
	}

}
