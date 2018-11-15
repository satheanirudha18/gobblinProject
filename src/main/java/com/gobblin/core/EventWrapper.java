package com.gobblin.core;

import com.gobblin.eventful.RefineInputs;

import org.apache.commons.lang3.StringUtils;
import gobblin.configuration.ConfigurationKeys;
import gobblin.configuration.WorkUnitState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anirudha Sathe on 13/11/18.
 */
public class EventWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventWrapper.class);

    public static Map<String, String> argv = new HashMap<String, String>();

    private String inputTypeUrl;

    private String searchInputs;

    private String searchURL = "search?";

    private static final String EVENTFUL_APP_KEY = "eventful.app.key";

    private static final String SEARCH_TYPE = "search.type";

    private static final String CUSTOM_INPUT_TYPE = "custom.input.type";

    private static final String EVENTS_KEYWORDS = "events.keywords";

    private static final String EVENTS_LOCATION = "events.location";

    private static final String EVENTS_DATE = "events.date";

    private static final String EVENTS_CATEGORY = "events.category";

    private static final String EVENTS_EXCLUDE_CATEGORY = "events.exclude.category";

    private static final String EVENTS_WITHIN = "events.within";

    private static final String EVENTS_UNITS = "events.units";

    private static final String EVENTS_COUNT_ONLY = "events.count.only";

    private static final String EVENTS_PAGE_SIZE = "events.page.size";

    private static final String EVENTS_PAGE_NUMBER = "events.page.number";

    private static final String EVENTS_SORT_ORDER = "events.sort.order";

    private static final String EVENTS_SORT_DIRECTION = "events.sort.direction";

    private static final String EVENTS_IMAGE_SIZE = "events.image.size";

    private static final String EVENTS_LANGUAGE = "events.language";

    private static final String EVENTS_MATURE = "events.mature";

    private static final String EVENTS_INCLUDE = "events.include";

    private static final String EVENTS_CHANGE_MULTI_DAY_START = "events.change.multi.day.start";

    private static final String VENUES_KEYWORDS = "venues.keywords";

    private static final String VENUES_LOCATION = "venues.location";

    private static final String VENUES_COUNT_ONLY = "venues.count.only";

    private static final String VENUES_PAGE_SIZE = "venues.page.size";

    private static final String VENUES_PAGE_NUMBER = "venues.page.number";

    private static final String VENUES_SORT_ORDER = "venues.sort.order";

    private static final String VENUES_SORT_DIRECTION = "venues.sort.direction";

    private static final String VENUES_WITHIN = "venues.within";

    private static final String VENUES_UNITS = "venues.units";

    private String eventful_api_rooturl;

    private String eventful_app_key;

    private String search_type;

    private WorkUnitState workUnitState;

    public EventWrapper(WorkUnitState workUnitState) {

        this.workUnitState = workUnitState;

    }

    private void getVenuesProperties(WorkUnitState workUnitState) {
        String venues_keywords = readProp(VENUES_KEYWORDS, workUnitState);
        if (!StringUtils.isEmpty(venues_keywords)){
            argv.put("keywords", venues_keywords);
        }

        String venues_location = readProp(VENUES_LOCATION, workUnitState);
        if (!StringUtils.isEmpty(venues_location)){
            argv.put("location", venues_location);
        }

        String venues_within = readProp(VENUES_WITHIN, workUnitState);
        if (!StringUtils.isEmpty(venues_within)){
            argv.put("within", venues_within);
        }

        String venues_units = readProp(VENUES_UNITS, workUnitState);
        if (!StringUtils.isEmpty(venues_units)){
            argv.put("units", venues_units);
        }

        String venues_count_only = readProp(VENUES_COUNT_ONLY, workUnitState);
        if (!StringUtils.isEmpty(venues_count_only)){
            argv.put("count_only", venues_count_only);
        }

        String venues_page_size = readProp(VENUES_PAGE_SIZE, workUnitState);
        if (!StringUtils.isEmpty(venues_page_size)){
            argv.put("page_size", venues_page_size);
        }

        String venues_page_number = readProp(VENUES_PAGE_NUMBER, workUnitState);
        if (!StringUtils.isEmpty(venues_page_number)){
            argv.put("page_number", venues_page_number);
        }

        String venues_sort_order = readProp(VENUES_SORT_ORDER, workUnitState);
        if (!StringUtils.isEmpty(venues_sort_order)){
            argv.put("sort_order", venues_sort_order);
        }

        String venues_sort_direction = readProp(VENUES_SORT_DIRECTION, workUnitState);
        if (!StringUtils.isEmpty(venues_sort_direction)){
            argv.put("sort_direction", venues_sort_direction);
        }
    }

    private void getEventsProperties(WorkUnitState workUnitState) {
        String events_keywords = readProp(EVENTS_KEYWORDS, workUnitState);
        if (!StringUtils.isEmpty(events_keywords)){
            argv.put("keywords", events_keywords);
        }

        String events_location = readProp(EVENTS_LOCATION, workUnitState);
        if (!StringUtils.isEmpty(events_location)){
            argv.put("location", events_location);
        }

        String events_date = readProp(EVENTS_DATE, workUnitState);
        if (!StringUtils.isEmpty(events_date)){
            argv.put("date", events_date);
        }

        String events_category = readProp(EVENTS_CATEGORY, workUnitState);
        if (!StringUtils.isEmpty(events_category)){
            argv.put("category", events_category);
        }

        String events_exclude_category = readProp(EVENTS_EXCLUDE_CATEGORY, workUnitState);
        if (!StringUtils.isEmpty(events_exclude_category)){
            argv.put("ex_category", events_exclude_category);
        }

        String events_within = readProp(EVENTS_WITHIN, workUnitState);
        if (!StringUtils.isEmpty(events_within)){
            argv.put("within", events_within);
        }

        String events_units = readProp(EVENTS_UNITS, workUnitState);
        if (!StringUtils.isEmpty(events_units)){
            argv.put("units", events_units);
        }

        String events_count_only = readProp(EVENTS_COUNT_ONLY, workUnitState);
        if (!StringUtils.isEmpty(events_count_only)){
            argv.put("count_only", events_count_only);
        }

        String events_page_size = readProp(EVENTS_PAGE_SIZE, workUnitState);
        if (!StringUtils.isEmpty(events_page_size)){
            argv.put("page_size", events_page_size);
        }

        String events_page_number = readProp(EVENTS_PAGE_NUMBER, workUnitState);
        if (!StringUtils.isEmpty(events_page_number)){
            argv.put("page_number", events_page_number);
        }

        String events_sort_order = readProp(EVENTS_SORT_ORDER, workUnitState);
        if (!StringUtils.isEmpty(events_sort_order)){
            argv.put("sort_order", events_sort_order);
        }

        String events_sort_direction = readProp(EVENTS_SORT_DIRECTION, workUnitState);
        if (!StringUtils.isEmpty(events_sort_direction)){
            argv.put("sort_direction", events_sort_direction);
        }

        String events_image_size = readProp(EVENTS_IMAGE_SIZE, workUnitState);
        if (!StringUtils.isEmpty(events_image_size)){
            argv.put("image_sizes", events_image_size);
        }

        String events_language = readProp(EVENTS_LANGUAGE, workUnitState);
        if (!StringUtils.isEmpty(events_language)){
            argv.put("languages", events_language);
        }

        String events_mature = readProp(EVENTS_MATURE, workUnitState);
        if (!StringUtils.isEmpty(events_mature)){
            argv.put("mature", events_mature);
        }

        String events_include = readProp(EVENTS_INCLUDE, workUnitState);
        if (!StringUtils.isEmpty(events_include)){
            argv.put("include", events_include);
        }

        String events_change_multi_day_start = readProp(EVENTS_CHANGE_MULTI_DAY_START, workUnitState);
        if (!StringUtils.isEmpty(events_change_multi_day_start)){
            argv.put("change_multi_day_start", events_change_multi_day_start);
        }
    }

    public String getCustomProperties() {
        this.eventful_api_rooturl = ConfigurationKeys.SOURCE_FILEBASED_FILES_TO_PULL;

        this.eventful_app_key = readProp(EVENTFUL_APP_KEY, this.workUnitState);
        argv.put("app_key", this.eventful_app_key);

        this.search_type = readProp(SEARCH_TYPE, this.workUnitState);

        if (search_type.equalsIgnoreCase("events")){
            inputTypeUrl = readProp(CUSTOM_INPUT_TYPE, this.workUnitState);
            getEventsProperties(workUnitState);
        }else if (search_type.equalsIgnoreCase("venues")) {
            inputTypeUrl = readProp(CUSTOM_INPUT_TYPE, this.workUnitState);
            getVenuesProperties(workUnitState);
        }else {
            LOGGER.error("Invalid search.type property. It can be either events or venues - ", search_type);
        }

        RefineInputs refineInputs = new RefineInputs();

        searchInputs = refineInputs.refineSearchInputs(argv);

        String url = eventful_api_rooturl + inputTypeUrl + search_type + searchURL + searchInputs;

        return url;
    }

    private String readProp(String key, WorkUnitState workUnitState) {
        String value = workUnitState.getProp(key);

        LOGGER.info(key + " = " + value);

        return value;
    }
}
