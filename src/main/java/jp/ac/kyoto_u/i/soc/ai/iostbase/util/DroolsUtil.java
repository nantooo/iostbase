package jp.ac.kyoto_u.i.soc.ai.iostbase.util;

import java.io.IOException;
import java.io.InputStream;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsUtil {
	public static KieSession createSessionFromResource(Package pkg, String ruleClassPath) throws IOException{
		return createSessionFromResource(
				"/" + pkg.getName().replaceAll("\\.", "/") + "/" + ruleClassPath);
	}

	public static KieSession createSessionFromResource(String ruleClassPath)
	throws IOException{
		try(InputStream is = DroolsUtil.class.getResourceAsStream(ruleClassPath)){
			return createSessionFromResource(is, ruleClassPath);
		}
	}

	public static KieSession createSessionFromResource(InputStream body, String rulePath)
	throws IOException{
		KieServices kieServices = KieServices.Factory.get();
		KieFileSystem kfs = kieServices.newKieFileSystem();
		if(rulePath.length() > 0 && rulePath.charAt(0) != '/') {
			rulePath = "/" + rulePath;
		}
		kfs.write("src/main/resources" + rulePath,
				kieServices.getResources().newInputStreamResource(body));
		KieBuilder kieBuilder = kieServices.newKieBuilder( kfs ).buildAll();
		Results results = kieBuilder.getResults();
		if( results.hasMessages( Message.Level.ERROR ) ){
			System.out.println( results.getMessages() );
			throw new RuntimeException("### errors ###" );
		}
		KieContainer kieContainer = kieServices.newKieContainer(
				kieServices.getRepository().getDefaultReleaseId() );
		KieBase kieBase = kieContainer.getKieBase();
		return kieBase.newKieSession();
	}

	public static KieSession createStreamSessionFromResource(Package pkg, String rulePath) throws IOException{
		return createStreamSessionFromResource(
				"/" + pkg.getName().replaceAll("\\.", "/") + "/" + rulePath);
	}
	public static KieSession createStreamSessionFromResource(String rulePath)
	throws IOException{
		KieServices kieServices = KieServices.Factory.get();
		KieFileSystem kfs = kieServices.newKieFileSystem();
		try(InputStream is = DroolsUtil.class.getResourceAsStream(rulePath)){
			// for each DRL file, referenced by a plain old path name:
			kfs.write("src/main/resources" + rulePath,
					kieServices.getResources().newInputStreamResource(is));
			KieBuilder kieBuilder = kieServices.newKieBuilder( kfs ).buildAll();
			Results results = kieBuilder.getResults();
			if( results.hasMessages( Message.Level.ERROR ) ){
				System.out.println( results.getMessages() );
				throw new RuntimeException("### errors ###" );
			}
			KieContainer kieContainer = kieServices.newKieContainer(
					kieServices.getRepository().getDefaultReleaseId() );
			KieBaseConfiguration config = KieServices.Factory.get().newKieBaseConfiguration();
			config.setOption( EventProcessingOption.STREAM );
			KieBase kieBase = kieContainer.newKieBase(config);
			return kieBase.newKieSession();
		}
	}
}
