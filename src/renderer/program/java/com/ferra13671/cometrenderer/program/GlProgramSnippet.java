package com.ferra13671.cometrenderer.program;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import com.ferra13671.cometrenderer.utils.tag.TagEntry;

/**
 * Фрагмент программы, который может быть добавлен любой программе.
 * Позволяет более удобным способом добавлять в множество программ одну и ту же информацию.
 *
 * @param registry реестр данных, которые были созданы в сборщике программы.
 *
 * @see GlProgram
 * @see GlProgramSnippet
 */
public record GlProgramSnippet(Registry registry) {

    /**
     * Применяет фрагмент программы к сборщику программы.
     *
     * @param builder сборщик программы.
     * @param <T> тип объекта, используемого как путь к контенту шейдеров.
     */
    public <T> void applyTo(GlProgramBuilder<T> builder) {
        this.registry.forEachTags(tag ->
            processTag(tag, builder)
        );
    }

    private <S, T> void processTag(Tag<T> tag, GlProgramBuilder<S> builder) {
        TagEntry<T> tagEntry = this.registry.get(tag).orElseThrow();

        if (tag == CometTags.NAME)
            builder.name(CometTags.NAME.map(tagEntry.getValue()));
        else
        if (tag == CometTags.SHADERS)
            CometTags.SHADERS.map(tagEntry.getValue()).forEach((type, glslFileEntry) -> builder.shader(glslFileEntry, type));
        else
        if (tag == CometTags.UNIFORMS)
            CometTags.UNIFORMS.map(tagEntry.getValue()).forEach(builder::uniform);
        else
            builder.tag(tag, tagEntry.getValue());
    }
}