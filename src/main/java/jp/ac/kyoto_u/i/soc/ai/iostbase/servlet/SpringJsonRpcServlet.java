package jp.ac.kyoto_u.i.soc.ai.iostbase.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jp.go.nict.langrid.commons.lang.function.SoftException;
import jp.go.nict.langrid.servicecontainer.handler.jsonrpc.JsonRpcDynamicHandler;
import jp.go.nict.langrid.servicecontainer.handler.jsonrpc.JsonRpcHandler;

public class SpringJsonRpcServlet extends jp.go.nict.langrid.servicecontainer.handler.jsonrpc.servlet.JsonRpcServlet {
	@Autowired
	private AutowireCapableBeanFactory bf; 

	@Autowired
	private PlatformTransactionManager txm;

	@Override
	protected JsonRpcHandler createDefaultHandler() {
		return new JsonRpcDynamicHandler() {
			@Override
			protected void initialize(String serviceName, Object service) {
				bf.autowireBean(service);
				bf.initializeBean(service, serviceName);
			}
		};
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			new TransactionTemplate(txm).execute(s -> {
				try{
					super.service(req, res);
					return null;
				} catch(IOException | ServletException e) {
					throw new SoftException(e);
				}
			});
		} catch(SoftException e) {
			Throwable t = e.getCause();
			if(t instanceof IOException){
				throw (IOException)t;
			}
			if(t instanceof ServletException){
				throw (ServletException)t;
			}
			throw e;
		}
	}

	private static final long serialVersionUID = -6359200620349218955L;
}
