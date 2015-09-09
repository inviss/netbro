package kr.co.d2net.command;

import javax.servlet.http.HttpServletRequest;

import kr.co.d2net.exception.CommandException;

public interface Command {
	public String execute(HttpServletRequest req) throws CommandException;
}
