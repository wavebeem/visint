/* Copyright 2010 Brian Mock
 *
 *  This file is part of visint.
 *
 *  visint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  visint is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with visint.  If not, see <http://www.gnu.org/licenses/>.
 */

import javax.media.opengl.GL;
import com.sun.opengl.util.*;
import java.nio.*;
import java.util.*;

/**
 * This class draws a rectangle based on the given points.
 * @author Brian Mock
 */
public class Rect {
    private float[] p1;
    private float[] p2;
    private float[] p3;
    private float[] p4;

    private float[] normal;

    //private float[] color = Colors.GREY;
    private float[] color = Colors.YELLOW;
    private float[] zeroC = Colors.PURPLE;

    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer  colorBuffer;
    private FloatBuffer  blackBuffer;

    private float[] outlineColor;

    private float thisOutlineAlpha = 0.75f;

    public
    Rect(
        float[] p1, float[] p2,
        float[] p3, float[] p4
    ) {
        outlineColor = Colors.withAlpha(Colors.GREEN, thisOutlineAlpha);

        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;

        float[] tmp1 = Util.displacementVector(p2, p1);
        float[] tmp2 = Util.displacementVector(p2, p3);

        normal = Util.normalize(Util.cross(tmp1, tmp2));

        makeBuffers();
    }

    public float[][]
    getVertices() {
        return new float[][] {
            p1, p2, p3, p4
        };
    }

    private void
    makeBuffers() {
        allocBuffers();

        vertexBuffer.put(p1);
        vertexBuffer.put(p2);
        vertexBuffer.put(p3);
        vertexBuffer.put(p4);

        normalBuffer.put(normal);
        normalBuffer.put(normal);
        normalBuffer.put(normal);
        normalBuffer.put(normal);

        makeColBuffer();

        blackBuffer = Util.bigColorBuffer(outlineColor, 4);

        vertexBuffer.rewind();
        normalBuffer.rewind();
        colorBuffer .rewind();
    }

    /** Makes the color buffer based on function y-values. */
    private void
    makeColBuffer() {
        float[][] ps = {p1, p2, p3, p4};

        for (float[] p: ps) {
            if (p[1] != 0) {
                colorBuffer.put(color);
            }
            else {
                colorBuffer.put(zeroC);
            }
        }
    }

    private void
    allocBuffers() {
        // 4 points in 3-space,
        // 4 normals in 3-space,
        // 4 colors with 4 components each
        vertexBuffer = BufferUtil.newFloatBuffer(4 * 3);
        normalBuffer = BufferUtil.newFloatBuffer(4 * 3);
        colorBuffer  = BufferUtil.newFloatBuffer(4 * 4);
    }

    private void
    loadColor(GL gl) {
        gl.glColorPointer(4, gl.GL_FLOAT, 0,  colorBuffer);
    }

    private void
    loadBlack(GL gl) {
        gl.glColorPointer(4, gl.GL_FLOAT, 0,  blackBuffer);
    }

    public void
    draw(GL gl) {
        vertexArraySetup(gl);

        loadColor(gl);
        drawArrays(gl, gl.GL_QUADS );

        loadBlack(gl);
        drawArrays(gl, gl.GL_LINES );
        drawArrays(gl, gl.GL_POINTS);

        vertexArrayUnsetup(gl);
    }

    private void
    drawArrays(GL gl, int drawType) {
        gl.glDrawArrays(
            drawType,
            0, vertexBuffer.capacity()
        );
    }

    private void
    vertexArraySetup(GL gl) {
        gl.glEnableClientState(gl.GL_COLOR_ARRAY);
        gl.glEnableClientState(gl.GL_NORMAL_ARRAY);
        gl.glEnableClientState(gl.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, gl.GL_FLOAT, 0, vertexBuffer);
        gl.glNormalPointer(   gl.GL_FLOAT, 0, normalBuffer);
    }

    private void
    vertexArrayUnsetup(GL gl) {
        gl.glDisableClientState(gl.GL_COLOR_ARRAY);
        gl.glDisableClientState(gl.GL_NORMAL_ARRAY);
        gl.glDisableClientState(gl.GL_VERTEX_ARRAY);
    }
}
