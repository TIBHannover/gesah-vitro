/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.web.directives;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

public abstract class BaseTemplateDirectiveModel implements TemplateDirectiveModel {

    private static final Log log = LogFactory.getLog(BaseTemplateDirectiveModel.class);

    public abstract Map<String, Object> help(String name);

    public static String processTemplateToString(String templateName, Map<String, Object> map, Environment env) {
        Template template = getTemplate(templateName, env);
        StringWriter sw = new StringWriter();
        try {
            template.process(map, sw);
        } catch (TemplateException e) {
            log.error("Template Exception creating processing environment", e);
        } catch (IOException e) {
            log.error("IOException creating processing environment", e);
        }
        return sw.toString();
    }

    private static Template getTemplate(String templateName, Environment env) {
        Template template = null;
        try {
            template = env.getConfiguration().getTemplate(templateName);
        } catch (IOException e) {
            // RY Should probably throw this error instead.
            log.error("Cannot get template " + templateName, e);
        }
        return template;
    }

    // ----------------------------------------------------------------------
	// Convenience methods for parsing the parameter map
	// ----------------------------------------------------------------------

    /** Get the parameter, or throw an exception. */
	protected String getRequiredSimpleScalarParameter(Map<?, ?> params,
			String name) throws TemplateModelException {
		Object o = params.get(name);
		if (o == null) {
			throw new TemplateModelException("The '" + name
					+ "' parameter is required" + ".");
		}

		if (!(o instanceof SimpleScalar)) {
			throw new TemplateModelException("The '" + name
					+ "' parameter must be a string value.");
		}

		return o.toString();
	}

	/** Get the parameter, or "null" if the parameter is not provided. */
	protected String getOptionalSimpleScalarParameter(Map<?, ?> params,
			String name) throws TemplateModelException {
		Object o = params.get(name);
		if (o == null) {
			return null;
		}

		if (!(o instanceof SimpleScalar)) {
			throw new TemplateModelException("The '" + name
					+ "' parameter must be a string value.");
		}

		return o.toString();
	}

    protected Map<String, Object> getOptionalHashModelParameter(Map<?, ?> params, String name) throws TemplateModelException {
        Object object = params.get(name);
        if (object == null) {
            return Collections.emptyMap();
        }
        if (!(object instanceof TemplateHashModelEx)) {
            throw new TemplateModelException(String.format("The %s parameter must be a string value.", name));
        }
        TemplateHashModelEx hashModel = (TemplateHashModelEx) object;
        Map<String, Object> map = new HashMap<>();

        TemplateModelIterator it = hashModel.keys().iterator();
        while (it.hasNext()) {
            TemplateModel key = it.next();
            TemplateModel value = hashModel.get(key.toString());
            map.put(key.toString(), value);
        }
        return map;
    }
}
