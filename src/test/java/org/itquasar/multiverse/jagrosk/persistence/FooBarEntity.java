package org.itquasar.multiverse.jagrosk.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Created by guilherme on 31/01/17.
 */
@Entity
public class FooBarEntity implements JagroskEntity<Integer> {

    @Id
    @Column
    private Integer id;

    @Column
    private String fooBar;

    private FooBarEntity(){}

    public FooBarEntity(Integer id) {
        this.id = id;
    }

    public FooBarEntity(Integer id, String fooBar) {
        this.id = id;
        this.fooBar = fooBar;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getFooBar() {
        return fooBar;
    }

    public void setFooBar(String fooBar) {
        this.fooBar = fooBar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FooBarEntity that = (FooBarEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fooBar, that.fooBar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fooBar);
    }

    @Override
    public String toString() {
        return "FooBarEntity{" +
                "id=" + id +
                (fooBar == null ? "" : ", fooBar=" + fooBar) +
                '}';
    }
}
