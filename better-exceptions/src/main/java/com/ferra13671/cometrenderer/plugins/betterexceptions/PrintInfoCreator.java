package com.ferra13671.cometrenderer.plugins.betterexceptions;

import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.utils.tag.Registry;

public interface PrintInfoCreator<T extends CometException> {

    Registry create(T exception);
}
