package nextstep.subway.section;

import nextstep.subway.line.Sections;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SectionEventHandler {
    @EventListener
    public void handleSectionAdded(SectionAddedEvent event) {
        Section section = event.getSection();
        Sections sections = event.getSections();
        List<Section> sectionList = sections.getSections();

        // 구간이 없을 때.
        if(sectionList.isEmpty()) {
            sections.addSection(section);
        }
        // 가장 앞에 구간을 등록할 때
        Long firstUpStationId = sectionList.get(0).getUpStationId();
        if( firstUpStationId == section.getDownStationId()) {
            addSectionToFirst(event);
        }
        // 마지막에 구간을 등록할 때
        Long lastStationId = sectionList.get(sectionList.size() -1).getDownStationId();
        if( lastStationId == section.getUpStationId()) {
            addSectionToLast(event);
        }
        /**
         * 중간에 구간을 등록할 때:
         * 1. 하행역이 노선에 등록되어 있어야 하고
         * 2. 상행역이 노선에 등록되어 있으면 안된다.
         */
        boolean isUpStationEnrolled = false;
        boolean isDownStationEnrolled = false;
        for(Section item: sectionList) {
            if(item.getUpStationId() == section.getUpStationId() || item.getDownStationId() == section.getUpStationId()) {
                isUpStationEnrolled = true;
            }

            if(item.getDownStationId() == section.getUpStationId()) {
                isDownStationEnrolled = true;
            }
        }
        // 상행역은 등록이 되어있지 않고 하행역은 등록되어 있을 때만 구간을 추가한다
        if(!isUpStationEnrolled&&isDownStationEnrolled) {
            addSectionToMiddle(event);
        }


    }

    private void addSectionToFirst(SectionAddedEvent event) {

    }

    private void addSectionToMiddle(SectionAddedEvent event) {

    }

    private void addSectionToLast(SectionAddedEvent event) {

    }
}
