package com.library.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDateTime;

public class DateTag extends TagSupport {

    private static final Logger LOG = LogManager.getLogger(DateTag.class);

    @Override
    public int doStartTag() throws JspException {
        final LocalDateTime localDateTime = LocalDateTime.now();
        try {
            final JspWriter out = pageContext.getOut();
            out.write( "Today: " + localDateTime.toString());
        } catch (IOException e) {
            LOG.error("Jsp exception from doStartTag TagID = {}", id);
            LOG.error(e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
