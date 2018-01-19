package io.nuls.core.event;

import io.nuls.core.constant.ErrorCode;
import io.nuls.core.exception.NulsException;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.module.BaseModuleBootstrap;
import io.nuls.core.utils.io.NulsByteBuffer;
import io.nuls.core.utils.log.Log;
import io.nuls.core.utils.param.AssertUtil;

import java.util.*;

/**
 * @author Niels
 * @date 2017/11/7
 */
public class EventManager {
    private static final Map<String, Class<? extends BaseEvent>> EVENT_MAP = new HashMap<>();
    private static final Set<Class<? extends BaseEvent>> EVENT_CLASSES = new HashSet<>();

    public static void care(Class<? extends BaseEvent> busDataClass) {
        boolean b = EVENT_CLASSES.contains(busDataClass);
        if (!b) {
            throw new NulsRuntimeException(ErrorCode.FAILED, "EventClass is not legal:" + busDataClass.getName());
        }
    }

    public static void putEvent(Class<? extends BaseEvent> clazz) {
        try {
            if (EVENT_MAP.values().contains(clazz)) {
                return;
            }
            BaseEvent event = clazz.newInstance();
            putEvent(event.getHeader().getModuleId(), event.getHeader().getEventType(), clazz);
        } catch (InstantiationException e) {
            Log.error(e);
        } catch (IllegalAccessException e) {
            Log.error(e);
        }
    }

    private static void putEvent(short moduleId, short type, Class<? extends BaseEvent> clazz) {
        if (type == 0) {
            throw new NulsRuntimeException(ErrorCode.FAILED, "the event type cannot be 0!,module:" + moduleId + ",eventType:" + type);
        }
        if (EVENT_MAP.containsKey(moduleId + "_" + type)) {
            return;
        }
        EVENT_MAP.put(moduleId + "_" + type, clazz);
        cacheDataClass(clazz);
    }

    private static void cacheDataClass(Class<? extends BaseEvent> clazz) {
        EVENT_CLASSES.add(clazz);
        if (!clazz.getSuperclass().equals(BaseEvent.class)) {
            cacheDataClass((Class<? extends BaseEvent>) clazz.getSuperclass());
        }
    }

    public static BaseEvent getInstance(byte[] bytes) throws NulsException {
        EventHeader header = new EventHeader();
        header.parse(new NulsByteBuffer(bytes));
        Class<? extends BaseEvent> clazz = EVENT_MAP.get(header.getModuleId() + "_" + header.getEventType());
        BaseEvent event = null;
        try {
            event = clazz.newInstance();
        } catch (Exception e) {
            Log.error(e);
            throw new NulsException(ErrorCode.DATA_PARSE_ERROR);
        }
        event.parse(new NulsByteBuffer(bytes));
        return event;
    }

}