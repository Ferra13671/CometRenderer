package com.ferra13671.cometrenderer.minecraft.font;

import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.RenderColor;
import com.ferra13671.gltextureutils.Pair;
import lombok.Getter;
import org.apiguardian.api.API;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@API(status = API.Status.MAINTAINED, since = "2.8")
public class FormattedText {
    @Getter
    private final List<Pair<Supplier<RenderColor>, Character[]>> components = new ArrayList<>();

    public FormattedText(String s) {
        RenderColor currentColor = RenderColor.WHITE;
        boolean listenFormat = false;

        List<Character> characters = new ArrayList<>();

        for (char _char : s.toCharArray()) {
            if (_char == '§') {
                listenFormat = true;
                continue;
            }

            if (listenFormat) {
                RenderColor c = CRM.getColorFromMinecraftCode(_char);

                if (c != null) {
                    RenderColor color = currentColor;
                    this.components.add(new Pair<>(() -> color, characters.toArray(new Character[0])));
                    characters.clear();

                    currentColor = c;
                }

                listenFormat = false;
                continue;
            }

            characters.add(_char);
        }
        if (!characters.isEmpty()) {
            RenderColor color = currentColor;
            this.components.add(new Pair<>(() -> color, characters.toArray(new Character[0])));
            characters.clear();
        }
    }

    public FormattedText append(String text) {
        return append(new FormattedText(text));
    }

    public FormattedText append(FormattedText formattedText) {
        this.components.addAll(formattedText.getComponents());

        return this;
    }

    public FormattedText append(Supplier<RenderColor> color, String text) {
        this.components.add(new Pair<>(color, text.chars().mapToObj(c -> (char) c).toList().toArray(new Character[0])));

        return this;
    }
}
