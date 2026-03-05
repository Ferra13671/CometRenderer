package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GlslContent;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.exceptions.impl.LoadGlslContentException;
import com.ferra13671.cometrenderer.glsl.GlProgramBuilder;
import com.ferra13671.cometrenderer.glsl.GlProgramSnippet;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "1.1")
public abstract class CometLoader<T> {

    @API(status = API.Status.MAINTAINED, since = "1.3")
    public GlProgramBuilder<T> createProgramBuilder(GlProgramSnippet... snippets) {
        return new GlProgramBuilder<>(this, snippets);
    }

    @API(status = API.Status.MAINTAINED, since = "1.8.2")
    public GlslFileEntry createGlslFileEntry(String name, T path) {
        return new GlslFileEntry(name, GlslContent.fromString(getContent(path)), GlobalCometCompiler.DEFAULT_GLSL_FILE_ENTRY, new Registry());
    }

    @API(status = API.Status.MAINTAINED, since = "1.9")
    public String getContent(T path) {
        String content = null;
        try {
            content = load(path);
        } catch (Exception e) {
            CometRenderer.getExceptionManager().manageException(new LoadGlslContentException(e));
        }
        return content;
    }

    @API(status = API.Status.MAINTAINED, since = "1.9")
    public abstract String load(T path) throws Exception;
}
