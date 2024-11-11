package com.rws.ps;

import lombok.Data;

import java.util.List;

@Data
public class XOTrigger {
    private String name;
    private String urlParam;
    private boolean isMultiSelect;
    private List<String> values;
}
