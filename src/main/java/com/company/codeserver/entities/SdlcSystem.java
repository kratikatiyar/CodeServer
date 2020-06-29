package com.company.codeserver.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sdlc_system")
@EntityListeners(AuditingEntityListener.class)
public class SdlcSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @URL
    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    @CreatedDate
    private Instant createdDate;

    @Column(name = "last_modified_date", nullable = false)
    @UpdateTimestamp
    @LastModifiedDate
    private Instant lastModifiedDate;

    @Override
    public String toString() {
        return "{" + "\"id\":" + id + ", \"baseUrl\":\"" + baseUrl +  "\", \"description\":\""
            + description +  "\", \"createdDate\":\"" + createdDate + "\", \"lastModifiedDate\":\""
            + lastModifiedDate + "\"}";
    }
}
