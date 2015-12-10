package org.jasig.cas.config.environment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Data;

@Entity
@Data
@Table(name="cas_environment")
public class CasEnvironment {

    @Id
    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    public CasEnvironment() {
    }

    public CasEnvironment(String key) {
    	this.key = key;
    }

	public CasEnvironment(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
