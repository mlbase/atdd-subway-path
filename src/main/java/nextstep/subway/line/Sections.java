package nextstep.subway.line;


import nextstep.subway.commons.ErrorCode;
import nextstep.subway.commons.HttpException;
import nextstep.subway.section.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.PERSIST)
    @OrderColumn(name = "section_order")
    @JoinColumn(name = "line_id")
    private List<Section> sections = new ArrayList<>();

    /**
     * 순서도: 구간이 비었는지 확인 -> 비었으면 검증없이 추가 -> 상행역이 하행종점역인지 확인
     * -> 하행종점역일 경우 등록하는 하행역만 검증하고 추가 -> 아닐경우 처음에 추가하는지 중간에 추가하는 지 확인
     */
    public void addSection(Section section) {
        if(sections.isEmpty()) {
            initiateSection(section);
            return;
        }
        validateCreateSection(section);
        //마지막 섹션을 꺼낸다
        Long insertingDistance = section.getDistance();

        //상행역 인덱스를 찾는다.
        int targetIndex = getSectionByUpStationId(section);
        Section targetSection = sections.get(targetIndex);

        Long downSectionDownStationId = targetSection.getDownStationId();

        Long grossDistance = targetSection.getDistance();
        Long downSectionDistance = grossDistance - insertingDistance;
        targetSection.changeDownStationAndDistance(section.getDownStationId(), insertingDistance);

        //등록하는 상행역이 기존 구간에 존재하면 경우 추가한다

        //섹션 상태 변경 일어나야 함.

        //섹션 추가해야 함.
        Section creatingSection = Section.builder()
                .upStationId(targetSection.getDownStationId())
                .downStationId(downSectionDownStationId)
                .distance(downSectionDistance)
                .build();

        sections.add(creatingSection);
    }

    private void initiateSection(Section section) {
        sections.add(section);
    }

    private void addSectionToFirst(Section section) {

    }

    private void addSectionToMiddle(Section section) {

    }

    private void addSectionToLast(Section section) {

    }

    private int getSectionByUpStationId(Section section) {
        int index = 0;
        // 상행역에서 등록하려는 상핵역이 존재하는 지 찾기
        for (Section item : sections) {
            if (item.getUpStationId().equals(section.getUpStationId())) {
                return index;
            }
            index++;
        }
        throw new HttpException(ErrorCode.UP_STATION_INVALID);
    }


    public void deleteSection(Long stationId) {
        validateDeleteSection(stationId);
        sections.remove(sections.size() - 1);
    }

    private void validateCreateSection(Section creatingSection) {
        // 하행 종점이 같은 것이 존재하면 x
        boolean isSameDownTerminalExist = sections.stream().anyMatch(section -> section.getDownStationId().equals(creatingSection.getDownStationId()));
        if (isSameDownTerminalExist) {
            throw new HttpException(ErrorCode.DOWN_STATION_INVALID, creatingSection.getDownStationId().toString());
        }


    }

    private void validateDeleteSection(Long stationId) {
        // 마지막 구간 제거 금지
        if (sections.size() == 1) {
            throw new HttpException(ErrorCode.CANNOT_REMOVE_LAST_SECTION);
        }
        // 제거하는 역이 하행종점이 아니면 x
        Section lastSection = sections.get(sections.size() - 1);
        if (lastSection.getDownStationId() != stationId) {
            throw new HttpException(ErrorCode.IS_NOT_TERMINAL_STATION, stationId.toString());
        }

    }

    public List<Long> upStationList() {
        return sections.stream().map(Section::getUpStationId).collect(Collectors.toList());
    }

    public List<Long> downStationList() {
        return sections.stream().map(Section::getDownStationId).collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
