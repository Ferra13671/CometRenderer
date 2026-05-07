package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.IndexBufferGenerator;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;

@API(status = API.Status.MAINTAINED, since = "2.5")
@UtilityClass
public class CustomDrawMode {

    public final DrawMode CUBE = DrawMode.builder()
            .glId(GL11.GL_TRIANGLES)
            .indexGeneration(
                    new IndexBufferGenerator(8, 36, (indexConsumer, firstVertexIndex) -> {
                        //1
                        indexConsumer.accept(firstVertexIndex);
                        indexConsumer.accept(firstVertexIndex + 1);
                        indexConsumer.accept(firstVertexIndex + 2);
                        indexConsumer.accept(firstVertexIndex + 2);
                        indexConsumer.accept(firstVertexIndex + 3);
                        indexConsumer.accept(firstVertexIndex);

                        //2
                        indexConsumer.accept(firstVertexIndex + 4);
                        indexConsumer.accept(firstVertexIndex + 5);
                        indexConsumer.accept(firstVertexIndex + 6);
                        indexConsumer.accept(firstVertexIndex + 6);
                        indexConsumer.accept(firstVertexIndex + 7);
                        indexConsumer.accept(firstVertexIndex + 4);

                        //3
                        indexConsumer.accept(firstVertexIndex);
                        indexConsumer.accept(firstVertexIndex + 3);
                        indexConsumer.accept(firstVertexIndex + 4);
                        indexConsumer.accept(firstVertexIndex + 4);
                        indexConsumer.accept(firstVertexIndex + 7);
                        indexConsumer.accept(firstVertexIndex);

                        //4
                        indexConsumer.accept(firstVertexIndex + 2);
                        indexConsumer.accept(firstVertexIndex + 1);
                        indexConsumer.accept(firstVertexIndex + 6);
                        indexConsumer.accept(firstVertexIndex + 6);
                        indexConsumer.accept(firstVertexIndex + 5);
                        indexConsumer.accept(firstVertexIndex + 2);

                        //5
                        indexConsumer.accept(firstVertexIndex + 3);
                        indexConsumer.accept(firstVertexIndex + 2);
                        indexConsumer.accept(firstVertexIndex + 5);
                        indexConsumer.accept(firstVertexIndex + 5);
                        indexConsumer.accept(firstVertexIndex + 4);
                        indexConsumer.accept(firstVertexIndex + 3);

                        //6
                        indexConsumer.accept(firstVertexIndex + 1);
                        indexConsumer.accept(firstVertexIndex);
                        indexConsumer.accept(firstVertexIndex + 7);
                        indexConsumer.accept(firstVertexIndex + 7);
                        indexConsumer.accept(firstVertexIndex + 6);
                        indexConsumer.accept(firstVertexIndex + 1);
                    }),
                    vertices -> vertices / 8 * 36
            )
            .build();

    public final DrawMode CUBE_OUTLINE = DrawMode.builder()
            .glId(GL11.GL_LINES)
            .indexGeneration(
                    new IndexBufferGenerator(8, 24, (indexConsumer, firstVertexIndex) -> {
                        //1
                        indexConsumer.accept(firstVertexIndex);
                        indexConsumer.accept(firstVertexIndex + 1);

                        //2
                        indexConsumer.accept(firstVertexIndex + 1);
                        indexConsumer.accept(firstVertexIndex + 2);

                        //3
                        indexConsumer.accept(firstVertexIndex + 2);
                        indexConsumer.accept(firstVertexIndex + 3);

                        //4
                        indexConsumer.accept(firstVertexIndex + 3);
                        indexConsumer.accept(firstVertexIndex);

                        //5
                        indexConsumer.accept(firstVertexIndex + 7);
                        indexConsumer.accept(firstVertexIndex + 6);

                        //6
                        indexConsumer.accept(firstVertexIndex + 6);
                        indexConsumer.accept(firstVertexIndex + 5);

                        //7
                        indexConsumer.accept(firstVertexIndex + 5);
                        indexConsumer.accept(firstVertexIndex + 4);

                        //8
                        indexConsumer.accept(firstVertexIndex + 4);
                        indexConsumer.accept(firstVertexIndex + 7);

                        //9
                        indexConsumer.accept(firstVertexIndex);
                        indexConsumer.accept(firstVertexIndex + 7);

                        //10
                        indexConsumer.accept(firstVertexIndex + 3);
                        indexConsumer.accept(firstVertexIndex + 4);

                        //11
                        indexConsumer.accept(firstVertexIndex + 2);
                        indexConsumer.accept(firstVertexIndex + 5);

                        //12
                        indexConsumer.accept(firstVertexIndex + 1);
                        indexConsumer.accept(firstVertexIndex + 6);
                    }),
                    vertices -> vertices / 8 * 24
            )
            .build();
}
