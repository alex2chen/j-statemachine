package com.kxtx.fsm.builder.state;

import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.builder.exec.Action;
import com.kxtx.fsm.builder.state.support.AbstractState;

import java.util.List;

/**
 * 状态生命周期（退出&进入）
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/27
 */
public interface StateLiftcycle<T extends StateMachine<T, S, E, C>, S, E, C> extends StateActivity<T, S, E, C> {
    AbstractState<T, S, E, C> addEntryAction(Action entryAction);
    AbstractState<T, S, E, C> addExitAction(Action exitAction);

    List<Action<T, S, E, C>> getEntryActions();
    List<Action<T, S, E, C>> getExitActions();
}
