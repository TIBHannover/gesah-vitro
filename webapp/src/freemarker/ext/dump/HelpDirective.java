/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package freemarker.ext.dump;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

public class HelpDirective extends BaseDumpDirective {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {

        if (loopVars.length != 0) {
            throw new TemplateModelException(
                "The help directive doesn't allow loop variables.");
        }
        if (body != null) {
            throw new TemplateModelException(
                "The help directive doesn't allow nested content.");
        }
        
        Object o = params.get("for");
        
        if ( o == null) {
            throw new TemplateModelException(
                "Must specify 'for' argument.");
        }
        
        if ( !(o instanceof SimpleScalar)) {
            throw new TemplateModelException(
               "Value of parameter 'for' must be a string.");     
        }
        
        String varName = o.toString(); //((SimpleScalar)o).getAsString();  
        TemplateHashModel dataModel = env.getDataModel();    
        Object templateModel = dataModel.get(varName);

        if (templateModel == null) {
            throw new TemplateModelException(
                "Value of parameter '" + varName + "' must be the name of a directive or method");              
        }

        if (! (templateModel instanceof TemplateMethodModel || templateModel instanceof TemplateDirectiveModel)) {
            throw new TemplateModelException(
                "Value of parameter '" + varName + "' must be the name of a directive or method");            
        }

        Map<String, Object> map = getTemplateVariableDump(varName, env);
        dump(TEMPLATE_DEFAULT, map, env);         
    }
    
    @Override
    protected Map<String, Object> help(String name) {
        Map<String, Object> map = new HashMap<String, Object>();

        //map.put("name", name);
        
        map.put("effect", "Output help for a directive or method.");
        
        //map.put("comments", "");
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("for", "name of directive or method");
        map.put("params", params);
        
        List<String> examples = new ArrayList<String>();
        examples.add("<@" + name + " for=\"dump\" />");
        examples.add("<@" + name + " for=\"profileUrl\" />");
        map.put("examples", examples);
        
        return map;
    }

}