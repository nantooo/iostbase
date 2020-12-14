package jp.ac.kyoto_u.i.soc.ai.iostbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class IostbaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(IostbaseApplication.class, args);
	}

}
