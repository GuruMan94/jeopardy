package ge.tsotne.jeopardy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ge.tsotne.jeopardy.Utils;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@Audited
@EntityListeners(AuditingEntityListener.class)
public class AuditedEntity {
    @JsonIgnore
    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    protected LocalDateTime createdAt;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    protected LocalDateTime lastModifiedAt;

    @JsonIgnore
    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    protected Long createdBy;

    @JsonIgnore
    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    protected Long lastModifiedBy;

    @JsonIgnore
    @Column(name = "ACTIVE", nullable = false, columnDefinition = "boolean default true")
    private Boolean active;

    @PrePersist
    public void prePersist() {
        this.active = true;
        Long userId = Utils.getCurrentUserId();
        this.createdBy = userId;
        this.lastModifiedBy = userId;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedBy = Utils.getCurrentUserId();
    }

    @PreRemove
    public void preRemove() {
        this.lastModifiedBy = Utils.getCurrentUserId();
    }
}
