/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.web.methods;

import java.util.List;

import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.RdfLiteralHash;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class DataPropertyEditKeyMethod implements TemplateMethodModelEx {

    @Override
    public Object exec(List args) throws TemplateModelException {
        if (args.size() < 4 || args.size() > 5) {
            throw new TemplateModelException("Wrong number of arguments");
        }
        String subjectUri = (String) args.get(0);
        String propertyUri = (String)args.get(1);
        String propertyTypeUri = (String)args.get(2);
        String value = (String) args.get(4);
        String lang = "";
        if (args.size() == 5) {
            lang = (String) args.get(5);
        }
        Integer editKey = RdfLiteralHash.makeRdfLiteralHash(subjectUri, propertyUri, propertyTypeUri, value, lang);
        return editKey;
    }
}
