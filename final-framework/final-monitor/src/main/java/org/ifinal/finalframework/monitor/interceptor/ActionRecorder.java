package org.ifinal.finalframework.monitor.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.ifinal.finalframework.json.Json;
import org.ifinal.finalframework.monitor.action.Action;
import org.ifinal.finalframework.monitor.action.ActionListener;
import org.ifinal.finalframework.monitor.executor.Recorder;
import org.ifinal.finalframework.util.Asserts;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Primary
@Component
public class ActionRecorder implements Recorder {

    private final List<ActionListener> listeners = new ArrayList<>();

    public ActionRecorder(ObjectProvider<List<ActionListener>> handlerProvider) {
        final List<ActionListener> handlers = handlerProvider.getIfAvailable();
        if (Asserts.nonEmpty(handlers)) {
            this.listeners.addAll(handlers);
        }
    }

    @Override
    public void record(Action action) {
        logger.info("{}", Json.toJson(action));
    }

}
