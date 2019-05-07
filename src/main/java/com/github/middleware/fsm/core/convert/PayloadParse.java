package com.github.middleware.fsm.core.convert;

import com.github.middleware.fsm.builder.machine.dto.RequestPayload;

import java.util.Optional;

/**
 * 适用于：PreChecker,Action
 * 此处暂时使用反模式，目的提高可重用性及泛型的繁琐性
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
@Deprecated
public interface PayloadParse {
    default <R extends RequestPayload> Optional<R> parse(RequestPayload requestPayload) {
        if (requestPayload == null) {
            return Optional.empty();
        }
        return Optional.of((R) requestPayload);
    }
}
