package kr.co.d2net.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.d2net.command.AddCommand;
import kr.co.d2net.command.Command;
import kr.co.d2net.exception.CommandException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseServlet extends HttpServlet {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = -2104920309488593716L;
	private Map<String, Object> commands = new HashMap<String, Object>();

	private ServletContext context;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();
		initCommands();
	}

	/**
	 * get, post 모두 
	 */
	public void service(HttpServletRequest req, HttpServletResponse res) throws
	ServletException, IOException {
		String cmd = req.getParameter("cmd");
		if(StringUtils.isNotBlank(cmd)) {
			String jsp = "";
			try {
				Command command = lookupCommand(cmd);
				jsp = command.execute(req);
			} catch (CommandException e) {
				logger.error("lookup error", e);
			}

			RequestDispatcher rd = req.getRequestDispatcher(jsp); 
			rd.forward(req,res);
		}
	}

	@SuppressWarnings("unused")
	private Command lookupCommand(String cmd) 
			throws CommandException{
		if(cmd == null) cmd = "add";
		if(commands.containsKey(cmd.toLowerCase())) 
			return (Command)commands.get(cmd.toLowerCase());
		else
			throw new CommandException("Command not Found!!");
	}

	@SuppressWarnings("unused")
	private void initCommands() {
		commands.put("add", new AddCommand("add.jsp"));
	}

}
