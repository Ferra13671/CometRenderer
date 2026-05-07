package com.ferra13671.cometrenderer.plugins.betterexceptions;

import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL)
public interface PrintInfoCreator<T extends CometException> {

    Registry create(T exception);
}
