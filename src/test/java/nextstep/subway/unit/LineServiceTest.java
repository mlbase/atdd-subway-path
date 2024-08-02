package nextstep.subway.unit;

import nextstep.subway.commons.HttpException;
import nextstep.subway.line.*;
import nextstep.subway.section.SectionCreateDTO;
import nextstep.subway.section.SectionCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(LineService.class)
@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Sql({"/subway/LineInsert.sql", "/subway/StationInsert.sql"})
class LineServiceTest {


    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("지하철 노선 생성 service test")
    @DirtiesContext
    void createLine() {
        // given
        LineCreateRequest request = new LineCreateRequest("3호선", "orange", 2L, 5L, 40L);

        //when
        Line line = lineService.createLine(request);

        //then
        assertEquals(line.getName(), request.getName());

    }

    @Test
    @DisplayName("지하철 단일 노선 조회 service test")
    @DirtiesContext
    void readLine() {
        //given
        Long testingId = 3L;

        //when
        Line line = lineService.readLine(testingId);

        //then
        assertEquals(line.getId(), testingId);
    }

    @Test
    @DisplayName("노선 목록 조회 service test")
    @DirtiesContext
    void readLines() {
        //given
        int testingCount = 3;

        //when
        List<Line> lines = lineService.readLines();

        //then
        assertEquals(lines.size(), testingCount);

    }

    @Test
    @DisplayName("노선 수정 service test")
    @DirtiesContext
    void updateLine() {
        //given
        Long testingId = 2L;
        LineUpdateRequest request = new LineUpdateRequest("3호선", "orange");
        LineUpdateDTO dto = new LineUpdateDTO(testingId, request);

        //when
        lineService.updateLine(dto);
        Line result = lineRepository.findById(testingId).orElse(null);

        //then
        assertEquals(result.getName(), dto.getName());
    }

    @Test
    @DisplayName("노선 삭제 service test")
    @DirtiesContext
    void deleteLine() {
        //given
        Long deletingId = 3L;

        //when
        lineService.deleteLine(deletingId);
        Optional<Line> result = lineRepository.findById(deletingId);

        //then
        assertEquals(null, result.orElse(null));

    }

    /**
     * given: controller 에서 넘어온 SectionCreateDto 가 존재하고
     * when: dto 를 바탕으로 section을 검증할 때
     * then: lineId 로 노선이 존재하지 않으면 HttpException 을 throw 한다.
     */
    @Test
    @DisplayName("구간 생성시 노선 검증 service test")
    void validateSectionCreate() {
        //given
        SectionCreateRequest request = new SectionCreateRequest(4L, 5L, 7L);
        Long testingId = 4L;
        SectionCreateDTO dto = new SectionCreateDTO(testingId, request);

        //when
        //then
        assertThrows(HttpException.class, () -> lineService.validateSectionCreate(dto));
    }

}