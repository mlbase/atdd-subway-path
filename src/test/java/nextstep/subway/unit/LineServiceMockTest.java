package nextstep.subway.unit;

import nextstep.subway.commons.ErrorCode;
import nextstep.subway.commons.HttpException;
import nextstep.subway.line.*;
import nextstep.subway.section.SectionCreateDTO;
import nextstep.subway.section.SectionCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
class LineServiceMockTest {

    @Mock
    LineRepository lineRepository;

    @InjectMocks
    LineService lineService;

    static LongStream randomLong() {
        return ThreadLocalRandom.current().longs(5, 1, 100);
    }

    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 생성된 노선이 반환된다.
     */
    @Test
    @DisplayName("지하철 노선 생성 service test")
    void createLine() {
        //given
        LineCreateRequest request = new LineCreateRequest("6호선", "blue", 3L, 4L, 60L);
        Line expectedLine = Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .upStationId(request.getUpStationId())
                .downStationId(request.getDownStationId())
                .distance(request.getDistance())
                .build();
        //when
        when(lineRepository.save(any(Line.class))).thenReturn(expectedLine);
        Line result = lineService.createLine(request);

        //then
        assertEquals(expectedLine, result);
        verify(lineRepository, times(1)).save(any(Line.class));
    }


    /**
     * given: 조회자가 id 를 알고 있고
     * when: id 로 조회를 할때
     * then: 단일 노선 정보를 반환한다.
     */
//    @Test
    @ParameterizedTest
    @MethodSource("randomLong")
    @DisplayName("지하철 노선 단일 조회 service 테스트")
    void readLine(Long id) {
        //given
        Line expectedLine = stubbingLine();
        //when
        when(lineRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(expectedLine));
        Line result = lineService.readLine(id);

        //then
        assertEquals(expectedLine, result);
        verify(lineRepository, times(1)).findById(id);
    }

    /**
     * given: 지하철 노선들이 등록 되어있고
     * when: 노선 목록을 조회하면
     * then: 모든 노선들이 조회된다.
     */
    @Test
    @DisplayName("지하철 노선 목록 조회 service 테스트")
    void readLines() {
        //given
        List<Line> expectedLines = stubbingLines();
        //when
        when(lineRepository.findAll()).thenReturn(stubbingLines());
        List<Line> result = lineService.readLines();

        //then
        assertEquals(expectedLines.size(), result.size());
        verify(lineRepository, times(1)).findAll();


    }

    /**
     * given: 해당 id로 지하철 노선이 있고
     * when: id 로 변경 요청을 하면
     * then: 노선 내용이 변경된다.
     */
    @ParameterizedTest
    @MethodSource("randomLong")
    @DisplayName("지하철 노선 업데이트 service 테스트")
    void updateLine(Long id) {
        //given
        LineUpdateRequest request = new LineUpdateRequest("7호선", "green");
        LineUpdateDTO dto = new LineUpdateDTO(id, request);

        //when
        when(lineRepository.findById(id)).thenReturn(Optional.of(stubbingLine()));
        lineService.updateLine(dto);

        //then
        verify(lineRepository, times(1)).findById(id);

    }

    /**
     * given: 해당 id 로 노선이 등록되어 있을 때
     * when: id 로 노선을 삭제하면
     * then: 노선이 삭제된다.
     */
    @ParameterizedTest
    @MethodSource("randomLong")
    @DisplayName("지하찰 노선 삭제 service 테스트")
    void deleteLine(Long id) {
        //given
        //when
        doNothing().when(lineRepository).deleteById(any(Long.class));
        lineService.deleteLine(id);
        //then
        verify(lineRepository, times(1)).deleteById(id);
    }

    /**
     * given: 노선이 등록되어 있고 구간 등록 요청이 발생했을 때
     * when: 지하철 노선을 조회하고
     * then: 지하철 노선이 등록되어 있지 않으면 해당 id 로 찾을 수 없다는 exception 이 발생한다.
     */
    @ParameterizedTest
    @MethodSource("randomLong")
    @DisplayName("구간 추가시 지하철 노선 검증 service 테스트")
    void validateSectionCreate(Long lineId) {
        //given
        SectionCreateRequest request = new SectionCreateRequest(3L, 5L, 5L);
        SectionCreateDTO dto = new SectionCreateDTO(lineId, request);

        //when
        when(lineRepository.findById(any(Long.class))).thenThrow(new HttpException(ErrorCode.MISSING_ID, lineId.toString()));

        //then
        assertThrows(HttpException.class, () -> lineService.validateSectionCreate(dto));

    }

    Line stubbingLine() {
        return Line.builder()
                .name("6호선")
                .color("purple")
                .upStationId(3L)
                .downStationId(4L)
                .distance(30L)
                .build();
    }

    List<Line> stubbingLines() {
        List<Line> lines = new ArrayList<>();
        Line item = stubbingLine();
        lines.add(item);
        item.changeName("7호선");
        item.changeColor("green");
        lines.add(item);
        return lines;
    }

}