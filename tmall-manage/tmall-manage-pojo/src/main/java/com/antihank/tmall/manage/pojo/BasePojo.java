package com.antihank.tmall.manage.pojo;

import java.io.Serializable;
import java.util.Date;

public abstract class BasePojo implements Serializable {

    private static final long serialVersionUID = -9030093840508027461L;
    private Date created;

    private Date updated;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
