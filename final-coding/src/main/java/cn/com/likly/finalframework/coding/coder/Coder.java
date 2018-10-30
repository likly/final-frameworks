package cn.com.likly.finalframework.coding.coder;

import cn.com.likly.finalframework.coding.annotation.Template;

import java.io.Writer;

/**
 * The generator of template code.
 * @author likly
 * @version 1.0
 * @date 2018-10-29 13:18
 * @since 1.0
 */
public interface Coder {
    /**
     * coding the template code with data model by writer.
     *
     * @param template the name of template
     * @param model    the data model of template
     * @param writer   the writer of coding file
     */
    void coding(String template, Object model, Writer writer);

    default void coding(Object model, Writer writer) {
        Template template = model.getClass().getAnnotation(Template.class);
        if (template == null) {
            throw new NullPointerException("the model must one Template annotation , model=" + model
                    .getClass()
                    .getName());
        }
        coding(template.value(), model, writer);
    }

}
