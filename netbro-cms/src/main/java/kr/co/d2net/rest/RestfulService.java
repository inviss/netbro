package kr.co.d2net.rest;

import java.util.Calendar;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import kr.co.d2net.dto.xml.WebRoot;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

@Path("/sample")
@Service("restfulService")
public class RestfulService {

	@GET
	@Path("/nowtime")
    @Produces(MediaType.TEXT_PLAIN)
	public String getNowTime() {
		DateFormatter formatter = new DateFormatter("yyyy-MM-dd hh:mm:ss");
		return formatter.print(Calendar.getInstance().getTime(), Locale.getDefault());
	}
	
	@GET
	@Path("/user/{id}")
    @Produces(MediaType.TEXT_PLAIN)
	public String getUser(@PathParam("id") String id) {
		return "request id: "+id;
	}
	
	@GET
	@Path("/user/{id}/{passwd}")
    @Produces(MediaType.TEXT_PLAIN)
	public String getUserPass(@PathParam("id") String id, @PathParam("passwd") String passwd) {
		return "request id: "+id+", passwd: "+passwd;
	}
	
	@GET
	@Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
	public String getUserPassQuery(@QueryParam("id") String id, @QueryParam("passwd") String passwd) {
		return "request id: "+id+", passwd: "+passwd;
	}
	
	@GET
	@Path("/web/{id}")
	@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.APPLICATION_XML+";charset=UTF-8"})
	public WebRoot getWebRoot(@PathParam("id") String id) {
		WebRoot root = new WebRoot();
		root.setVersion("1.1");
		root.setWriter(id);
		return root;
	}
		
}
