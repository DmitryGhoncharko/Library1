package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Author;
import com.library.service.BasicAuthorService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;

import java.util.List;

public class DeleteAuthorCommand implements Command {

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not delete author";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteAuthorCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long idAuthor = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        try {
            if (BasicAuthorService.getInstance().delete(idAuthor)) {
                final List<Author> authors = BasicAuthorService.getInstance().findAll();
                request.addAttributeToJsp(Constant.AUTHORS_ATTRIBUTE_NAME, authors);
                return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("url.author"));
            } else {
                request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
        return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
    }

    public static DeleteAuthorCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final DeleteAuthorCommand INSTANCE = new DeleteAuthorCommand();
    }
}
