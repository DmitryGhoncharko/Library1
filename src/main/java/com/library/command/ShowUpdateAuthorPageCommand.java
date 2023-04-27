package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Author;
import com.library.service.BasicAuthorService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ShowUpdateAuthorPageCommand implements Command{

    private static Logger LOG = LogManager.getLogger(ShowUpdateAuthorPageCommand.class);

    private static final String ERROR_PASS_MASSAGE_ATTRIBUTE = "Could not find author";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowUpdateAuthorPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long idAuthor = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        final Optional<Author> author;
        try {
            author = BasicAuthorService.getInstance().findById(idAuthor);
            request.addAttributeToJsp(Constant.AUTHOR_ATTRIBUTE_NAME, author.get());
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.update.author"));
        } catch (ServiceException e) {
            LOG.error("Service error, could not find update author", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MASSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ShowUpdateAuthorPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowUpdateAuthorPageCommand INSTANCE = new ShowUpdateAuthorPageCommand();
    }
}
