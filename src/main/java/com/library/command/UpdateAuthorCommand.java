package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Author;
import com.library.service.BasicAuthorService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UpdateAuthorCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(UpdateAuthorCommand.class);

    private static final String ERROR_PASS_MASSAGE_ATTRIBUTE = "Could not update author";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private UpdateAuthorCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long idAuthor = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        final String firstName = request.getParameter(Constant.FIRST_NAME_PARAMETER_NAME);
        final String lastName = request.getParameter(Constant.LAST_NAME_PARAMETER_NAME);
        try {
            final Optional<Author> updatedAuthor = BasicAuthorService.getInstance().update(idAuthor, firstName, lastName);
            if (updatedAuthor.isPresent()) {
                final List<Author> authors;
                authors = BasicAuthorService.getInstance().findAll();
                request.addAttributeToJsp(Constant.AUTHORS_ATTRIBUTE_NAME, authors);
                return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("url.author"));
            } else {
                request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MASSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
        } catch (ServiceException e) {
            LOG.error("Service error, could not update author", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MASSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static UpdateAuthorCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final UpdateAuthorCommand INSTANCE = new UpdateAuthorCommand();
    }
}
