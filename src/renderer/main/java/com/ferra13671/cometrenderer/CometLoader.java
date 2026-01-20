package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.program.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.program.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.exceptions.impl.LoadGlslContentException;
import com.ferra13671.cometrenderer.program.GlProgramBuilder;
import com.ferra13671.cometrenderer.program.GlProgramSnippet;

public abstract class CometLoader<T> {

    public GlProgramBuilder<T> createProgramBuilder(GlProgramSnippet... snippets) {
        return new GlProgramBuilder<>(this, snippets);
    }

    public GlslFileEntry createGlslFileEntry(String name, T path) {
        return new GlslFileEntry(name, getContent(path), GlobalCometCompiler.DEFAULT_FILE, new Registry());
    }

    public String getContent(T path) {
        String content = null;
        try {
            content = load(path);
        } catch (Exception e) {
            CometRenderer.getExceptionManager().manageException(new LoadGlslContentException(e));
        }
        return content;
    }

    public abstract String load(T path) throws Exception;
}
