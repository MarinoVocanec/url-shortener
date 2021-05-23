package vocanec.marino.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class which contains entry point for Url Shortener application.
 */
@SpringBootApplication
public class UrlshortenerApplication {

	/**
	 * Main function which gets called when program starts.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(UrlshortenerApplication.class, args);
	}

}
