package com.rws.ps;

import org.springframework.stereotype.Component;

import com.sdl.delivery.configuration.ConfigurationException;

import com.tridion.smarttarget.entitymodel.trigger.type.TriggerType;
import com.tridion.smarttarget.entitymodel.trigger.type.TriggerTypes;
import com.tridion.smarttarget.query.TriggerTypeProviderImpl;
import com.tridion.smarttarget.triggers.TriggerTypeProvider;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class XOTriggers {

    private final ConcurrentMap<String, XOTrigger> triggers = new ConcurrentHashMap<>();

    public XOTriggers() {
        readXOTriggers();
    }

    private void readXOTriggers() {
        try {
            TriggerTypeProvider triggerTypeProvider = new TriggerTypeProviderImpl();
            TriggerTypes triggerTypes = triggerTypeProvider.getTriggerTypes();
            for (TriggerType triggerType : triggerTypes.getTriggerTypes()) {
                XOTrigger trigger = new XOTrigger();
                trigger.setName(triggerType.getName());
                trigger.setUrlParam(triggerType.getUrlParam());
                trigger.setValues(triggerType.getListOfValues().getValues());
                triggers.put(triggerType.getUrlParam(), trigger);
            }
        }
        catch(ConfigurationException exception) {
            log.error(exception.getMessage());
        }
    }

    public void refreshXOTriggers() {
        readXOTriggers();
    }

    public XOTrigger getTrigger(String urlParam) {
        return triggers.get(urlParam);
    }
}
