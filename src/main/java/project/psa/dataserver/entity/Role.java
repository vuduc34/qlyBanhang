package project.psa.dataserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Table(name = "ROLE")
@Entity
@Getter
@Setter
public class Role {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "roleid", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Nhanvien> nhanviens;

    @Column(name = "ROLENAME", length = 20)
    private String rolename;

}