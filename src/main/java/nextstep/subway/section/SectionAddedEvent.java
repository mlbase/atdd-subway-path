package nextstep.subway.section;

import nextstep.subway.line.Line;
import nextstep.subway.line.Sections;
import org.springframework.context.ApplicationEvent;

public class SectionAddedEvent extends ApplicationEvent {
    private final Sections sections;
    private final Section section;
    private final Line line;

    public SectionAddedEvent(Sections sections, Section section, Line line) {
        super(sections);
        this.sections = sections;
        this.section = section;
        this.line = line;
    }

    public Sections getSections() {
        return sections;
    }

    public Section getSection() {
        return section;
    }

    public Line getLine() {
        return line;
    }
}
