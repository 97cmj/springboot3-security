package com.cmj.api.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "persistent_logins")
public class persistentLogins {

    @Id
    @Column(length = 64)
    private String series;
    @Column(nullable = false, length = 100)
    private String username;
    @Column(nullable = false, length = 64)
    private String token;
    @Column(nullable = false, length = 64)
    private String last_used;
}
