package org.itquasar.multiverse.jagrosk.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by guilherme on 31/01/17.
 */
@Entity
public class FooBarEntity implements JagroskEntity<Integer> {

    @Id
    @GeneratedValue
    @Column
    private Integer id;

    @Column
    private String fooBar;

    public FooBarEntity(Integer id) {
        this.id = id;
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
    public String toString() {
        return "FooBarEntity{" +
                "id=" + id +
                (fooBar == null ? "" : ", fooBar=" + fooBar) +
                '}';
    }
}
