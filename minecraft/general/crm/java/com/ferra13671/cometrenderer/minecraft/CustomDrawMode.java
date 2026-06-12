package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.IndexBufferGenerator;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;

@API(status = API.Status.MAINTAINED, since = "2.5")
@UtilityClass
public class CustomDrawMode {

    public final DrawMode CUBE = new DrawMode(
            GL11.GL_TRIANGLES,
            new IndexBufferGenerator(8, 36, (indexList, firstVertexIndex) ->
                indexList.indexes(
                        //1
                        firstVertexIndex,
                        firstVertexIndex + 1,
                        firstVertexIndex + 2,
                        firstVertexIndex + 2,
                        firstVertexIndex + 3,
                        firstVertexIndex,

                        //2
                        firstVertexIndex + 4,
                        firstVertexIndex + 5,
                        firstVertexIndex + 6,
                        firstVertexIndex + 6,
                        firstVertexIndex + 7,
                        firstVertexIndex + 4,

                        //3
                        firstVertexIndex,
                        firstVertexIndex + 3,
                        firstVertexIndex + 4,
                        firstVertexIndex + 4,
                        firstVertexIndex + 7,
                        firstVertexIndex,

                        //4
                        firstVertexIndex + 2,
                        firstVertexIndex + 1,
                        firstVertexIndex + 6,
                        firstVertexIndex + 6,
                        firstVertexIndex + 5,
                        firstVertexIndex + 2,

                        //5
                        firstVertexIndex + 3,
                        firstVertexIndex + 2,
                        firstVertexIndex + 5,
                        firstVertexIndex + 5,
                        firstVertexIndex + 4,
                        firstVertexIndex + 3,

                        //6
                        firstVertexIndex + 1,
                        firstVertexIndex,
                        firstVertexIndex + 7,
                        firstVertexIndex + 7,
                        firstVertexIndex + 6,
                        firstVertexIndex + 1
                )
            )
    );

    public final DrawMode CUBE_OUTLINE = new DrawMode(
            GL11.GL_LINES,
            new IndexBufferGenerator(8, 24, (indexList, firstVertexIndex) ->
                indexList.indexes(
                        //1
                        firstVertexIndex,
                        firstVertexIndex + 1,

                        //2
                        firstVertexIndex + 1,
                        firstVertexIndex + 2,

                        //3
                        firstVertexIndex + 2,
                        firstVertexIndex + 3,

                        //4
                        firstVertexIndex + 3,
                        firstVertexIndex,

                        //5
                        firstVertexIndex + 7,
                        firstVertexIndex + 6,

                        //6
                        firstVertexIndex + 6,
                        firstVertexIndex + 5,

                        //7
                        firstVertexIndex + 5,
                        firstVertexIndex + 4,

                        //8
                        firstVertexIndex + 4,
                        firstVertexIndex + 7,

                        //9
                        firstVertexIndex,
                        firstVertexIndex + 7,

                        //10
                        firstVertexIndex + 3,
                        firstVertexIndex + 4,

                        //11
                        firstVertexIndex + 2,
                        firstVertexIndex + 5,

                        //12
                        firstVertexIndex + 1,
                        firstVertexIndex + 6
                )
            )
            );
}
