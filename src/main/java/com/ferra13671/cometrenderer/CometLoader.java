package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.glsl.compiler.CometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLContent;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLFileEntry;
import com.ferra13671.cometrenderer.glsl.shader.GLShaderBuilder;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "1.1")
public abstract class CometLoader<T> {

    @API(status = API.Status.EXPERIMENTAL, since = "2.7")
    public GLShaderBuilder<T> createShaderBuilder() {
        return new GLShaderBuilder<>(this);
    }

    @API(status = API.Status.MAINTAINED, since = "1.8.2")
    public GLSLFileEntry createGLSLFileEntry(String name, T path) {
        return new GLSLFileEntry(name, GLSLContent.fromString(getContent(path)), CometCompiler.DEFAULT_GLSL_FILE_ENTRY, new Registry());
    }

    @API(status = API.Status.MAINTAINED, since = "1.9")
    public String getContent(T path) {
        String content = null;
        try {
            content = load(path);
        } catch (Exception e) {
            ErrorHandlers.onLoadGLSLContentException(e);
        }
        return content;
    }

    @API(status = API.Status.MAINTAINED, since = "1.9")
    public abstract String load(T path) throws Exception;
}
