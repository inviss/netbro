package kr.co.d2net.command;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.d2net.exception.CommandException;

public class AddCommand implements Command {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	String next;
	public AddCommand(String next) {
		this.next = next;
	}

	public String execute(HttpServletRequest req) 
			throws CommandException {
		logger.debug("execute");
		req.setAttribute("msg", "Request Added");
		
		return next;
	}

}
