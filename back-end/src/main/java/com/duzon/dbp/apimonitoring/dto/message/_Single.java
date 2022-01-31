package com.duzon.dbp.apimonitoring.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * _Single
 */
@Getter
@Setter
public class _Single<T> extends Common {
    private T data;
}