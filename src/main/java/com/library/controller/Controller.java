package com.library.controller;

import com.library.command.Command;
import com.library.command.CommandRequest;
import com.library.command.CommandResponse;
import com.library.connection.ConnectionPool;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/controller")
public class Controller extends HttpServlet {    

    private static final Logger LOG = LogManager.getLogger(Controller.class);


    private final RequestFactory requestFactory = RequestFactory.getInstance();

    public void init() {
        ConnectionPool.lockingPool().init();
    }

    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LOG.trace("doGet method");
        processRequest(httpServletRequest, httpServletResponse);
    }

    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LOG.trace("doPost method");
        processRequest(httpServletRequest, httpServletResponse);
    }

    private void processRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        final String commandName = httpServletRequest.getParameter(Constant.COMMAND_PARAMETER_NAME);
        final Command command = Command.of(commandName);
        final CommandRequest request = requestFactory.createRequest(httpServletRequest);
        final CommandResponse commandResponse = command.execute(request);
        forwardOrRedirectToCommandResponseLocation(httpServletRequest, httpServletResponse, commandResponse);
    }

    private void forwardOrRedirectToCommandResponseLocation(HttpServletRequest request, HttpServletResponse response, CommandResponse commandResponse) {
        try {
            if (commandResponse.isRedirect()) {
                response.sendRedirect(commandResponse.getPath());
            } else {
                final String pathForDispatcher = commandResponse.getPath();
                final RequestDispatcher dispatcher = request.getRequestDispatcher(pathForDispatcher);
                dispatcher.forward(request, response);
            }
        } catch (ServletException e) {
            LOG.error("servlet exception occurred", e);
        } catch (IOException e) {
            LOG.error("IO exception occurred", e);
        }
    }

    public void destroy() {
        ConnectionPool.lockingPool().shoutDown();
    }
}