package com.ferra13671.cometrenderer.vertex.format;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.VertexFormatOverflowException;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import org.apiguardian.api.API;

import java.util.ArrayList;
import java.util.List;

/**
 * Сборщик вершинного формата.
 *
 * @see VertexFormat
 */
@API(status = API.Status.MAINTAINED, since = "1.4")
public final class VertexFormatBuilder extends Builder<VertexFormat> {
    /** Список элементов вершин. **/
    private final List<VertexElement> vertexElements = new ArrayList<>();
    /** Список имен элементов вершин. **/
    private final List<String> elementNames = new ArrayList<>();

    public VertexFormatBuilder() {
        super("vertex format");
    }

    public VertexFormatBuilder element(String name, VertexElementType<?> type, int count) {
        return element(this.vertexElements.size(), name, type, count);
    }

    public VertexFormatBuilder element(int position, String name, VertexElementType<?> type, int count) {
        int id = this.vertexElements.size();
        this.elementNames.add(position, name);
        this.vertexElements.add(position, new VertexElement(id, count, type));

        int maxElements = CometRenderer.getRegistry().get(CometTags.MAX_VERTEX_ELEMENTS).orElseThrow().getValue();
        if (this.vertexElements.size() > maxElements)
            CometRenderer.getExceptionManager().manageException(new VertexFormatOverflowException(maxElements));

        return this;
    }

    public VertexFormatBuilder removeElement(String name) {
        for (int i = 0; i < this.elementNames.size(); i++) {
            if (this.elementNames.get(i).equals(name)) {
                this.elementNames.remove(i);
                this.vertexElements.remove(i);
            }
        }

        return this;
    }

    @Override
    public VertexFormat build() {
        return new VertexFormat(this.vertexElements, this.elementNames);
    }
}
