package com.ferra13671.cometrenderer.plugins.bettercompiler.processors;

import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.CompilerExtension;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLContent;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

import java.util.Map;

@API(status = API.Status.MAINTAINED, since = "3.1")
@UtilityClass
public class DefineProcessor {

    @Getter
    private final CompilerExtension extension = new CompilerExtension("better-compiler-define") {

        @Override
        public void processCompile(Registry shaderRegistry, Registry builderRegistry) {
            Map<String, String> definitions = builderRegistry.get(BetterCompilerTags.PROGRAM_INFO).orElseThrow().getDefinitions();

            if (!definitions.isEmpty()) {
                GLSLContent content = shaderRegistry.get(CometTags.CONTENT).orElseThrow();

                StringBuilder definitionsBuilder = new StringBuilder();
                for (String definitionName : definitions.keySet())
                    definitionsBuilder.append(String.format("#define %s %s\n", definitionName, definitions.get(definitionName)));

                int index = content.getLines()[0].startsWith("#version") ? 1 : 0;
                content.getLines()[index] = definitionsBuilder.toString().concat("\n").concat(content.getLines()[index]);
            }
        }
    };
}
