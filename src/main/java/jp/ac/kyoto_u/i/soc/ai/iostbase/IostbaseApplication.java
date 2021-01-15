package jp.ac.kyoto_u.i.soc.ai.iostbase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class IostbaseApplication {
	@SuppressWarnings("unused")
	@Autowired
	private Sessions sessions;

	public static void main(String[] args) {
		SpringApplication.run(IostbaseApplication.class, args);
	}
}
