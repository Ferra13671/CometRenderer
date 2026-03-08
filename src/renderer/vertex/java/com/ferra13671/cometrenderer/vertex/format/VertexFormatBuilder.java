package com.ferra13671.cometrenderer.vertex.format;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.VertexFormatOverflowException;
import com.ferra13671.cometrenderer.utils.Builder;
import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.gltextureutils.Pair;
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
    private final List<Pair<VertexElementType<?>, Integer>> elementsInfo = new ArrayList<>();
    /** Список имен элементов вершин. **/
    private final List<String> elementNames = new ArrayList<>();

    public VertexFormatBuilder() {
        super("vertex format");
    }

    public VertexFormatBuilder element(String name, VertexElementType<?> type, int count) {
        return element(this.elementsInfo.size(), name, type, count);
    }

    public VertexFormatBuilder element(int position, String name, VertexElementType<?> type, int count) {
        this.elementNames.add(position, name);
        this.elementsInfo.add(position, new Pair<>(type, count));

        int maxElements = CometRenderer.getRegistry().get(CometTags.MAX_VERTEX_ELEMENTS).orElseThrow().getValue();
        if (this.elementsInfo.size() > maxElements)
            CometRenderer.getExceptionManager().manageException(new VertexFormatOverflowException(maxElements));

        return this;
    }

    public VertexFormatBuilder removeElement(String name) {
        for (int i = 0; i < this.elementNames.size(); i++) {
            if (this.elementNames.get(i).equals(name)) {
                this.elementNames.remove(i);
                this.elementsInfo.remove(i);
            }
        }

        return this;
    }

    @Override
    public VertexFormat build() {
        List<VertexElement> elements = new ArrayList<>();
        for (int i = 0; i < this.elementNames.size(); i++) {
            Pair<VertexElementType<?>, Integer> info = this.elementsInfo.get(i);

            elements.add(new VertexElement(i, info.getRight(), info.getLeft()));
        }

        return new VertexFormat(elements, this.elementNames);
    }
}
