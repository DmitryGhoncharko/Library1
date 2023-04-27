package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.service.BasicBookService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteBookCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(DeleteBookCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not delete book";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteBookCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long id = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        try {
            if (BasicBookService.getInstance().delete(id)) {
                return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("url.catalog"));
            } else {
                LOG.error("could not delete book ");
                request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
        } catch (ServiceException e) {
            LOG.error("Service error, could not delete book ", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static DeleteBookCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final DeleteBookCommand INSTANCE = new DeleteBookCommand();
    }
}
