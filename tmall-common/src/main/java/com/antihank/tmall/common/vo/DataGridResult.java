package com.antihank.tmall.common.vo;

import java.util.List;

/**
 * Created by Antihank on 2017/5/6.
 */
public class DataGridResult<T> {
    private Long total;
    private List<T> rows;

    public DataGridResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
