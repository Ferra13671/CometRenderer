package com.ferra13671.cometrenderer.plugins.betterexceptions;

import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.exceptions.impl.*;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileProgramException;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileShaderException;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.*;
import com.ferra13671.cometrenderer.tag.Registry;

import java.util.HashMap;
import java.util.Map;

public class ExceptionsPrintInfo {
    private static final Map<Class<? extends CometException>, PrintInfoCreator<?>> exceptionsPrintInfo = new HashMap<>();
    static {
        add(CompileProgramException.class, exception -> makeRegistry(
                "Compile program error.",
                exception.getMessage(),
                new String[]{
                        "Comet Renderer malfunction",
                        "OpenGL malfunction (Very Rare)",
                        "Out of GPU Memory (Very Rare)"
                },
                new String[]{
                        "Contact Ferra13671",
                        "Check the integrity of the OpenGL library",
                        "Make sure you are not overloading the GPU memory (e.g. by loading something into the GPU in a infinity loop)"
                }
        ));
        add(CompileShaderException.class, exception -> makeRegistry(
                "Compile shader error.",
                exception.getMessage(),
                new String[]{
                        "Error in shader structure",
                        "Out of GPU Memory (Very Rare)"
                },
                new String[]{
                        "Check the shader structure for errors and fix them",
                        "Make sure you are not overloading the GPU memory (e.g. by loading something into the GPU in a infinity loop)"
                }
        ));
        add(BadVertexStructureException.class, exception -> makeRegistry(
                "Bad vertex structure.",
                exception.getMessage(),
                new String[]{
                        "When building vertex in VertexBuilder, you missed one or more elements."
                },
                new String[]{
                        "Check your vertex building method and fix it."
                }
        ));
        add(IllegalMeshBuilderStateException.class, exception -> makeRegistry(
                "Illegal MeshBuilder state.",
                exception.getDetails(),
                exception.getReasons(),
                exception.getSolutions()
        ));
        add(NoSuchVertexElementException.class, exception -> makeRegistry(
                "No such vertex element.",
                exception.getMessage(),
                new String[]{
                        "You may have misspelled the vertex element name.",
                        "When creating a VertexBuilder, you selected the wrong VertexBuilder."
                },
                new String[]{
                        "Check that you have written the names of the vertex elements correctly in both the vertex format builder and the vertex builder method.",
                        "Check that you selected the correct vertex format when creating VertexBuilder."
                }
        ));
        add(VertexOverflowException.class, exception -> makeRegistry(
                "VertexBuilder overflow.",
                exception.getMessage(),
                new String[]{
                        "You may be adding vertices in an infinite loop.",
                        "You're a monster who managed to manually exceed the maximum number of vertices."
                },
                new String[]{
                        "Check the method where you add vertices to VertexBuilder and fix it."
                }
        ));
        add(DoubleShaderAdditionException.class, exception -> makeRegistry(
                "Double shader addition in program builder.",
                exception.getMessage(),
                new String[]{
                        "Your program builder has an invalid structure.",
                        "You have added a program snippet to the program builder that already adds a shader of the required type."
                },
                new String[]{
                        "Check that your program builder structure is correct.",
                        "Check the program snippets you add to the program builder."
                }
        ));
        add(DoubleUniformAdditionException.class, exception -> makeRegistry(
                "Double uniform addition in program builder.",
                exception.getMessage(),
                new String[]{
                        "Your program builder has an invalid structure.",
                        "You have added a program snippet to the program builder that already adds a uniform of the required name.",
                        "When the shader library was included in shader, a uniform with this name was already added."
                },
                new String[]{
                        "Check that your program builder structure is correct.",
                        "Check the program snippets you add to the program builder.",
                        "Check which shader libraries are being included into shaders and fix problem with uniforms."
                }
        ));
        add(IllegalProgramBuilderArgumentException.class, exception -> makeRegistry(
                "Illegal argument in program builder.",
                exception.getMessage(),
                new String[]{
                        "You did not specify all the required arguments before building program builder",
                        "The argument you specified in program builder is null"
                },
                new String[]{
                        "Check if you are specifying all arguments when building library builder.",
                        "Check if you are not passing any arguments that are null."
                }
        ));
        add(LoadGlslContentException.class, exception -> makeRegistry(
                "Load glsl content error.",
                exception.getMessage(),
                new String[]{
                        "Error in content loader",
                        "Inability to access content",
                        "Another error"
                },
                new String[]{
                        "Make sure your content loader is working properly (if you are using a custom loader)",
                        "Make sure you store or have access to the glsl content",
                        "Check out the details"
                }
        ));
        add(NoSuchUniformException.class, exception -> makeRegistry(
                "No such uniform error.",
                exception.getMessage(),
                new String[]{
                        "The uniform added to the program schema is not present in program itself",
                        "You are trying to find a uniform that is not in the program schema"
                },
                new String[]{
                        "Make sure the uniform is in both schema and program",
                        "Make sure you are search the right uniform and have not made a mistake in its name"
                }
        ));
        add(WrongGpuBufferTargetException.class, exception -> makeRegistry(
                "Wrong gpu buffer target.",
                exception.getMessage(),
                new String[]{
                        "You are giving the method that caused the error an wrong gpu buffer"
                },
                new String[]{
                        "Recheck the method call that caused the error and fix the buffer issue"
                }
        ));
        add(UnsupportedShaderException.class, exception -> makeRegistry(
                "Unsupported shader error.",
                exception.getMessage(),
                new String[]{
                        "You are trying to add a shader to program that has a type that is not supported by the current version of OpenGL"
                },
                new String[]{
                        "Stop adding shader of this type to the program",
                        "If the project is designed for newer versions of OpenGL, then you can ignore this error",
                        "Disable version checking via the 'COMPARE_CURRENT_AND_SHADER_OPENGL_VERSIONS' tag"
                }
        ));
        add(VertexFormatOverflowException.class, exception -> makeRegistry(
                "VertexFormat overflow.",
                exception.getMessage(),
                new String[]{
                        "You are adding more elements to VertexFormat than the maximum number"
                },
                new String[]{
                        "Create a VertexFormat that uses fewer elements"
                }
        ));
        add(UnsupportedOpenGLVersionException.class, exception -> makeRegistry(
                "Unsupported OpenGL version.",
                exception.getMessage(),
                new String[]{
                        "Your GPU is too old and does not support the minimum OpenGL version required by CometRenderer"
                },
                new String[]{
                        "Just buy a new GPU"
                }
        ));
    }

    private static <T extends CometException> void add(Class<T> clazz, PrintInfoCreator<T> printInfoCreator) {
        exceptionsPrintInfo.put(clazz, printInfoCreator);
    }

    public static Map<Class<? extends CometException>, PrintInfoCreator<?>> getMap() {
        return exceptionsPrintInfo;
    }

    private static Registry makeRegistry(String description, String details, String[] reasons, String[] solutions) {
        Registry registry = new Registry();
        registry.setImmutable(BetterExceptionsPlugin.DESCRIPTION, description);
        registry.setImmutable(BetterExceptionsPlugin.DETAILS, details);
        registry.setImmutable(BetterExceptionsPlugin.REASONS, reasons);
        registry.setImmutable(BetterExceptionsPlugin.SOLUTIONS, solutions);

        return registry;
    }
}
