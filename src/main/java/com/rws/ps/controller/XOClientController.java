package com.rws.ps.controller;

import com.tridion.smarttarget.SmartTargetException;
import com.tridion.smarttarget.query.Promotion;
import com.tridion.smarttarget.query.ResultSet;
import com.tridion.smarttarget.query.builder.*;
import com.tridion.smarttarget.utils.TcmUri;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author pmedcraft
 */
@Controller
@Slf4j
public class XOClientController {

    @GetMapping("/")
    public String outputForm() {
        return "promotions-form";
    }

    @ResponseBody
    @PostMapping(value = {"/promotions"})
    public List<Promotion> getPromotions(@RequestParam("pubId") String pubId,
                                         @RequestParam("pageId") String pageId,
                                         @RequestParam("region") String region,
                                         @RequestParam("city") String city,
                                         @RequestParam("browser") String browser) throws ParseException, SmartTargetException {

        TcmUri publicationUri = new TcmUri(String.format("tcm:0-%s-1", pubId));
        TcmUri pageUri = new TcmUri(String.format("tcm:%s-%s-64", pubId, pageId));

        QueryBuilder queryBuilder = new QueryBuilder();

        final StringBuilder triggers = new StringBuilder();
        if (StringUtils.isNotEmpty(city)) {
            triggers.append(String.format("&Contact - City=%s", city));
        }
        if (StringUtils.isNotEmpty(browser)) {
            Arrays.stream(StringUtils.split(browser, ","))
                    .forEach(browserEntry -> triggers.append(String.format("&Visitor - Browser=%s", browserEntry)));
        }
        if (StringUtils.isNotEmpty(triggers))
            queryBuilder.parseQueryString(triggers.toString());

        queryBuilder
                .addCriteria(new PublicationCriteria(publicationUri))
                .addCriteria(new PageCriteria(pageUri));

        if (StringUtils.isNotEmpty(region))
            queryBuilder.addCriteria(new RegionCriteria(region));

        /*
        List<String> regions = List.of("Header", "Footer", "Sidebar", "Inset 1", "Inset 2");
        for (String region : regions) {
            queryBuilder.addCriteria(new RegionCriteria(region));
        }
        */

        ResultSet resultSet = queryBuilder.execute();
        if (resultSet != null) {
            log.info(String.format("Number of Promotions: %d", resultSet.getPromotions().size()));
            return resultSet.getPromotions();
        }

        return Collections.emptyList();
    }
}
