package jp.ac.kyoto_u.i.soc.ai.iostbase.servlet;

import javax.servlet.annotation.WebServlet;

import jp.ac.kyoto_u.i.soc.ai.iostbase.service.EventManagement;
import jp.go.nict.langrid.servicecontainer.handler.annotation.Service;
import jp.go.nict.langrid.servicecontainer.handler.annotation.Services;

@WebServlet("/jsServices/*")
@Services({
	@Service(name="EventManagement", impl=EventManagement.class)
})
public class JsonRpcServlet extends jp.go.nict.langrid.servicecontainer.handler.jsonrpc.servlet.JsonRpcServlet {
	public JsonRpcServlet() {
	}
	private static final long serialVersionUID = 7707078352790036143L;
}
