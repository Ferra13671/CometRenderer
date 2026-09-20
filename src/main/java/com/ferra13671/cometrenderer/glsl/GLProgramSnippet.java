package com.ferra13671.cometrenderer.glsl;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import org.apiguardian.api.API;

/**
 * Фрагмент программы, который может быть добавлен любой программе.
 * Позволяет более удобным способом добавлять в множество программ одну и ту же информацию.
 *
 * @param registry реестр данных, которые были созданы в сборщике программы.
 *
 * @see GLProgram
 * @see GLProgramSnippet
 */
@API(status = API.Status.EXPERIMENTAL, since = "1.1")
public record GLProgramSnippet(Registry registry) {

    /**
     * Применяет фрагмент программы к сборщику программы.
     *
     * @param builder сборщик программы.
     */
    @API(status = API.Status.INTERNAL)
    public void applyTo(GLProgramBuilder builder) {
        this.registry.forEachTags(tag ->
            processTag(tag, builder)
        );
    }

    private <T> void processTag(Tag<T> tag, GLProgramBuilder builder) {
        T tagValue = this.registry.get(tag).orElseThrow();

        if (tag == CometTags.NAME)
            builder.name(CometTags.NAME.map(tagValue));
        else
        if (tag == CometTags.COMPILED_SHADERS)
            CometTags.COMPILED_SHADERS.map(tagValue).forEach((type, shader) -> builder.shader(shader));
        else
        if (tag == CometTags.UNIFORMS)
            CometTags.UNIFORMS.map(tagValue).forEach(builder::uniform);
        else
            builder.tag(tag, tagValue);
    }
}