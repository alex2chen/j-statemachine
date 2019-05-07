package com.github.middleware.fsm.core.utils;

import com.github.middleware.fsm.core.Singletonable;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/6
 */
public interface IDProvider {
    String ID_FORMAT_PREFIX = "fsm-%s";

    String get();

    final class Default implements IDProvider, Singletonable {
        private static IDProvider instance;

        private Default() {
        }

        public static IDProvider getInstance() {
            if (instance == null) {
                instance = new Default();
            }
            return instance;
        }

        @Override
        public String get() {
            return String.format(ID_FORMAT_PREFIX, RandomStringUtils.randomAlphanumeric(12));
        }
    }

    final class UUIDProvider implements IDProvider, Singletonable {
        private static IDProvider instance;

        public static IDProvider getInstance() {
            if (instance == null) {
                instance = new UUIDProvider();
            }
            return instance;
        }

        @Override
        public String get() {
            return String.format(ID_FORMAT_PREFIX, UUID.randomUUID().toString().replace("-", ""));
        }
    }

}
