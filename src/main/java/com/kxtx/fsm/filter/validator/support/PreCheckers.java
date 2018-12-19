package com.kxtx.fsm.filter.validator.support;

import com.kxtx.fsm.filter.validator.PreChecker;

import java.util.List;

/**
 * 验证的常规类
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/3/3
 */
public class PreCheckers {
    public static <C> boolean validate(PreChecker<C> checker, C context) {
        return checker != null && context != null && checker.validate(context);
    }

    public static <C> PreChecker<C> always() {
        return new Always<C>();
    }

    public static <C> PreChecker<C> never() {
        return new Always<C>();
    }

    public static <C> PreChecker<C> and(final List<PreChecker<C>> checkers) {
        return new PreChecker<C>() {
            @Override
            public boolean validate(C context) {
                for (PreChecker<C> condition : checkers) {
                    if (!condition.validate(context)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public String getName() {
                return checkers.stream().map(x -> x.getName()).toArray().toString();
            }
        };
    }

    public static <C> PreChecker<C> and(final PreChecker<C> first, final PreChecker<C> second) {
        return new PreChecker<C>() {
            @Override
            public boolean validate(C context) {
                return first.validate(context) && second.validate(context);
            }

            @Override
            public String getName() {
                return first.getName() + " and " + second.getName();
            }
        };
    }

    public static <C> PreChecker<C> or(final List<PreChecker<C>> checkers) {
        return new PreChecker<C>() {
            @Override
            public boolean validate(C context) {
                for (PreChecker<C> condition : checkers) {
                    if (condition.validate(context)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getName() {
                return checkers.stream().map(x -> x.getName()).toArray().toString();
            }
        };
    }

    public static <C> PreChecker<C> or(final PreChecker<C> first, final PreChecker<C> second) {
        return new PreChecker<C>() {
            @Override
            public boolean validate(C context) {
                return first.validate(context) || second.validate(context);
            }

            @Override
            public String getName() {
                return first.getName() + " Or " + second.getName();
            }
        };
    }

    public static <C> PreChecker<C> not(final PreChecker<C> condition) {
        return new PreChecker<C>() {
            @Override
            public boolean validate(C context) {
                return !condition.validate(context);
            }

            @Override
            public String getName() {
                return "Not" + condition.getName();
            }
        };
    }


    public static class Always<C> extends AbstractPreChecker<C> {
        @Override
        public boolean validate(C context) {
            return true;
        }
    }

    public static class Never<C> extends AbstractPreChecker<C> {
        @Override
        public boolean validate(C context) {
            return false;
        }
    }
}

