package nextstep.subway.section;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private Long distance;

    @Column(name = "section_order")
    private Integer sectionOrder;

    public Section() {
    }

    public Section(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void changeDownStationAndDistance(Long downStationId, Long insertingDistance) {
        this.downStationId = downStationId;
        this.distance = insertingDistance;
    }

    public static class Builder {
        private Long upStationId;
        private Long downStationId;
        private Long distance;

        public Builder() {
        }

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Section build() {
            return new Section(this.upStationId, this.downStationId, this.distance);
        }


    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Integer getSectionOrder() {
        return sectionOrder;
    }
}
