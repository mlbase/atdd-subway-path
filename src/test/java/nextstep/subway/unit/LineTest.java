package nextstep.subway.unit;


import nextstep.subway.line.Line;
import nextstep.subway.line.Sections;
import nextstep.subway.section.Section;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LineTest {

    Section section;

    Sections sections;

    Line line;

    Long station1;
    Long station2;

    Long station3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        station1 = 1L;
        station2 = 5L;
        station3 = 6L;

        Section section1 = Section.builder()
                .upStationId(station1)
                .downStationId(station2)
                .distance(5L)
                .build();
        sections = new Sections();
        sections.addSection(section1);

        line = Line.builder()
                .name("9호선")
                .color("brown")
                .upStationId(station1)
                .downStationId(station2)
                .distance(5L)
                .sections(sections)
                .build();
    }


    /**
     * given: 지하철 노선이 등록 되어 있고
     * when: 역이름을 바꾸면
     * then: 객체에 등록된 역 이름이 바뀐다.
     */
    @Test
    @DisplayName("Line 객체 이름 바꾸기")
    void changeName() {
        //given
        String changingName = "8호선";

        //when
        line.changeName(changingName);

        //then
        assertEquals(line.getName(), changingName);
    }

    /**
     * given: 지하철 노선이 등록 되어 있고
     * when: 색깔을 바꾸면
     * then: 객체에 등록된 색깔이 바뀐다.
     */
    @Test
    @DisplayName("Line 객체 색깔 바꾸기")
    void changeColor() {
        //given
        String changingColor = "red";

        //when
        line.changeColor(changingColor);

        //then
        assertEquals(line.getColor(), changingColor);
    }


    @Nested
    @DisplayName("구간 추가 테스트")
    class AddTest {

        /**
         * given: 지하철 노선이 등록 되어 있고
         * when: 구간을 추가하면
         * then: 객체에 구간들에 새 구간이 추가된다.
         */
        @Test
        @DisplayName("Line 객체 구간 추가하기")
        void addSection() {
            // given
            section = Section.builder()
                    .upStationId(station2)
                    .downStationId(station3)
                    .distance(5L)
                    .build();


            //when
            line.addSection(section);

            //then
            assertThat(sections.getSections().contains(section));
        }

        /**
         * given: 노선이 존재하고 구간이 2개이상 존재할 때,
         * when: 하행종점역이 아닌 역을 상행역으로 추가해도
         * then: 노선에 구간을 추가할 수 있다.
         */
        @Test
        @DisplayName("Line 객체 하행 종점역이 아닌 구간 추가하기")
        void 하행종점역이_아닌역_추가하기() {
            Section addingSection = Section.builder()
                    .upStationId(station1)
                    .downStationId(station3)
                    .distance(2L)
                    .build();

            line.addSection(addingSection);
            line.upStationList();
            Assertions.assertAll(
                    () -> assertThat(line.upStationList()).containsExactly(station1, station3),
                    () -> assertThat(line.downStationList()).containsExactly(station3, station2));
        }

        @Test
        @DisplayName("Line 객체에서 중간에 구간 추가시 존재하지 않는 역은 추가할 수 없다.")
        void 존재하지_않는_역_추가하기_안됨() {

        }

        @Test
        @DisplayName("중간에 추가할 때 거리가 기존의 거리보다 같거나 큰 경우 추가할 수 없다.")
        void 기존_구간보다_큰_구간은_추가할수_없다() {

        }

        @Test
        @DisplayName("하행역 기존에 존재하는 경우 추가할 수 없다.")
        void 하행역_존재시_구간_추가할수없다() {

        }


    }

    @Nested
    @DisplayName("구간 조회 테스트")
    class ReadTest {


    }

    @Nested
    @DisplayName("삭제 테스트")
    class DeleteTest {

        /**
         * given: 지하철 노선이 등록 되어 있고
         * when: 구간을 제거하면
         * then: 객체에 구간들에 제거한 구간이 사라진다.
         */
        @Test
        @DisplayName("Line 객체 구간 제거하기")
        void deleteSection() {
            //given
            Long deletingStationId = 5L;
            doNothing().when(sections).deleteSection(deletingStationId);

            //when
            line.deleteSection(deletingStationId);

            //then
            verify(sections, times(1)).deleteSection(deletingStationId);
        }

    }
}



