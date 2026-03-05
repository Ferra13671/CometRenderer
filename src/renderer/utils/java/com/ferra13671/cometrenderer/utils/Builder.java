package com.ferra13671.cometrenderer.utils;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.exceptions.impl.IllegalBuilderArgumentException;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.AllArgsConstructor;
import org.apiguardian.api.API;

@AllArgsConstructor
@API(status = API.Status.INTERNAL)
public abstract class Builder<T> {
    protected final String builderName;

    @API(status = API.Status.MAINTAINED)
    public abstract T build();

    protected void assertNotNull(Object value, String valueName) {
        if (value == null)
            manageIllegalArgument(String.format("Missing %s in %s builder.", valueName, this.builderName));
    }

    protected void assertNotNull(Registry registry, Tag<?> tag) {
        if (!registry.contains(tag))
            manageIllegalArgument(String.format("Missing %s in %s builder.", tag.id(), this.builderName));
    }

    protected void manageIllegalArgument(String message) {
        CometRenderer.getExceptionManager().manageException(new IllegalBuilderArgumentException(this.builderName, message));
    }
}
