package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.exceptions.CometException;
import com.ferra13671.cometrenderer.exceptions.impl.*;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileProgramException;
import com.ferra13671.cometrenderer.exceptions.impl.compile.CompileShaderException;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.*;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

@UtilityClass
@API(status = API.Status.INTERNAL, since = "3.0")
public class ErrorHandlers {

    public void onWrongBufferTarget(int givenTarget, int requiredTaget) {
        new WrongGpuBufferTargetException(givenTarget, requiredTaget).printStackTrace();
    }

    public void onUnsupportedOpenGLVersion(GLVersion currentVersion, GLVersion minimumVersion) throws CometException {
        throw new UnsupportedOpenGLVersionException(currentVersion, minimumVersion);
    }

    public void onCompileProgramError(String programName, String reason) throws CometException {
        throw new CompileProgramException(programName, reason);
    }

    public void onCompileShaderError(String shaderName, String reason) throws CometException {
        throw new CompileShaderException(shaderName, reason);
    }

    public void onMeshBuilderAlreadyBuilt() {
        throw new IllegalStateException("MeshBuilder has been already built.");
    }

    public void onMeshBuilderNotBuildingVertex() {
        throw new IllegalStateException("Not currently building vertex.");
    }

    public void onEmptyMeshBuilder() {
        throw new IllegalStateException("MeshBuilder was empty.");
    }

    public void onIllegalBuilderArgument(String message) {
        throw new IllegalBuilderArgumentException(message);
    }

    public void onVertexOverflow(int maxVertices) {
        throw new VertexOverflowException(maxVertices);
    }

    public void onIndexOverflow(int maxIndices) {
        throw new IndexOverflowException(maxIndices);
    }

    public void onBadVertexStructure(String missingElements) {
        throw new BadVertexStructureException(missingElements);
    }

    public void onIllegalVertexElementStructure(VertexElementType<?> type, String reason) {
        new IllegalVertexElementStructureException(type, reason).printStackTrace();
    }

    public void onNoSuchVertexElement(String name) {
        throw new NoSuchVertexElementException(name);
    }

    public void onVertexFormatOverflow(int maxElements) {
        throw new VertexFormatOverflowException(maxElements);
    }

    public void onAllocatorOverflow(long needSize, long maxAllocatorSize, long freeSize) {
        throw new AllocatorOverflowException(needSize, maxAllocatorSize, freeSize);
    }

    public void onDoubleShaderAddition(String newShader, ShaderType type, String oldShader) {
        CometRenderer.getLogger().error(String.format(
                "An attempt was made to add shader '%s' with type '%s', while shader '%s' with the same type had already been added. Overwriting prev shader.",
                newShader,
                type.name(),
                oldShader
        ));
    }

    public void onDoubleUniformAddition(String uniformName) {
        CometRenderer.getLogger().error(String.format(
                "An attempt was made to add a uniform named '%s' that already exists. Overwriting prev uniform.",
                uniformName
        ));
    }

    public void onDoubleCompilerExtensionAddition(String name) {
        CometRenderer.getLogger().error(String.format(
                "Found 2 compiler extensions named %s, overwriting prev compiler extension.",
                name
        ));
    }

    public void onLoadShaderWithEmptyBuilder() {
        throw new IllegalStateException("Cannot load an uncompiled shader in builder without a loader.");
    }

    public void onLoadGLSLContentException(Exception e) {
        throw new LoadGLSLContentException(e);
    }

    public void onNoSuchUniform(String uniformName, String programName) {
        throw new NoSuchUniformException(uniformName, programName);
    }

    public void onUnsupportedShader(ShaderType shaderType) {
        throw new UnsupportedShaderException(shaderType);
    }
}
