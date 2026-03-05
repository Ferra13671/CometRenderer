package com.ferra13671.cometrenderer.exceptions;

import org.apiguardian.api.API;

/**
 * Тип действия, которое будет вызвано для ошибки
 */
@API(status = API.Status.EXPERIMENTAL, since = "2.5")
public enum ExceptionAction {
    Throw,
    Log,
    Ignore
}
