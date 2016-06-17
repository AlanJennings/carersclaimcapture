package uk.gov.dwp.carersallowance.controller.breaks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BreaksInCareData {
    public static List<Map<String, String>> sampleBreaks() {

        List<CareBreak> breaks = new ArrayList<>();

        breaks.add(new CareBreak(1,
                                 CareBreak.toDate("01/06/2016"),
                                 "15:00",
                                 CareBreak.WHERE_CAREE.OTHER.label(),
                                 "Caree somewhere else",
                                 CareBreak.WHERE_YOU.HOME.label(),
                                 "Carer somewhere else",
                                 "No",
                                 null,
                                 "",
                                 "Yes"));

        breaks.add(new CareBreak(2,
                                 CareBreak.toDate("12/05/2016"),
                                 "13:00",
                                 CareBreak.WHERE_CAREE.RESPITE.label(),
                                 "Caree somewhere else",
                                 CareBreak.WHERE_YOU.OTHER.label(),
                                 "Carer somewhere else",
                                 "Yes",
                                 CareBreak.toDate("14/05/2016"),
                                 "12:00",
                                 "Yes"));

        breaks.add(new CareBreak(3,
                                 CareBreak.toDate("20/04/2016"),
                                 "11:00",
                                 CareBreak.WHERE_CAREE.HOLIDAY.label(),
                                 "Caree somewhere else",
                                 CareBreak.WHERE_YOU.OTHER.label(),
                                 "Carer somewhere else",
                                 "Yes",
                                 CareBreak.toDate("28/04/2016"),
                                 "10:00",
                                 "No"));

        List<Map<String, String>> flattenedBreaks = new ArrayList<>();
        for(int index = 0; index < breaks.size(); index++) {
            CareBreak careBreak = breaks.get(index);
            Map<String, String> attributes = careBreak.toMap();
            flattenedBreaks.add(attributes);
        }

        return flattenedBreaks;
    }
}