package com.duzon.dbp.apimonitoring.dto.message;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * _List
 */
@Getter
@Setter
public class _List<T> extends Common {
    private List<T> list;
}
