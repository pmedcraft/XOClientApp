package com.rws.ps.criteria;

import com.rws.ps.XOTrigger;
import com.rws.ps.XOTriggers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

@Slf4j
public class GeoRegionParameterProvider {

    private final XOTriggers xoTriggers;
    private final String geoRegion;
    private final String country;

    public GeoRegionParameterProvider(XOTriggers xoTriggers, String geoRegion, String country) {
        this.xoTriggers = xoTriggers;
        this.geoRegion = geoRegion;
        this.country = country;
    }

    private String getParameterName() {
        return "contact_geoRegion";
    }

    public String getParameterValue() {
        List<String> values = new ArrayList<>();
        XOTrigger trigger = xoTriggers.getTrigger(getParameterName());
        for (String geoRegionTriggerValue : trigger.getValues()) {
            if (passesExclusionCriteria(geoRegionTriggerValue)) {
                values.add(geoRegionTriggerValue);
            }
        }

        return values.stream()
                .map(value -> String.format("&%s=%s", getParameterName(), value))
                .collect(Collectors.joining());
    }

    private boolean passesExclusionCriteria(String triggerValue) {
        if (StringUtils.isEmpty(country))
            return StringUtils.endsWith(triggerValue, geoRegion);

        return StringUtils.startsWith(triggerValue, geoRegion) &&
                !StringUtils.endsWith(triggerValue, String.format("NOT %s", country));
    }
}
