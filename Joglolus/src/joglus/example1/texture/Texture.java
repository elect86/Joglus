/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package joglus.example1.texture;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.GLBuffers;
import com.oculusvr.capi.OvrSizei;
import java.nio.FloatBuffer;
import javax.media.opengl.GL3;

/**
 *
 * @author gbarbieri
 */
public class Texture {

    private int[] id;
    private OvrSizei size;
    private int internalFormat;
    private int glFormat;
    private int glType;
    private FloatBuffer data;

    public Texture(int[] id, OvrSizei size, int internalFormat, int glFormat, int glType, FloatBuffer data) {
        this.id = id;
        this.size = size;
        this.internalFormat = internalFormat;
        this.glFormat = glFormat;
        this.glType = glType;
        this.data = data;
    }

    public static Texture create(GL3 gl3, int format, OvrSizei sizei, float[] data) {

        int glFormat, glType = GL3.GL_UNSIGNED_BYTE;

        switch (format & TextureFormat.TypeMask) {

            case TextureFormat.RGBA:
                glFormat = GL3.GL_RGBA;
                break;

            default:
                return null;
        }

        int[] textureId = new int[1];
        gl3.glGenTextures(1, textureId, 0);

        gl3.glBindTexture(GL3.GL_TEXTURE_2D, textureId[0]);

        boolean isSRGB = ((format & TextureFormat.TypeMask) == TextureFormat.RGBA && (format & TextureFormat.SRGB) != 0);
        boolean isDepth = ((format & TextureFormat.Depth) != 0);

        int internalFormat = isSRGB ? GL3.GL_SRGB_ALPHA : isDepth ? GL3.GL_DEPTH_COMPONENT32F : glFormat;

        FloatBuffer buffer = data != null ? GLBuffers.newDirectFloatBuffer(data) : null;

        gl3.glTexImage2D(GL3.GL_TEXTURE_2D, 0, internalFormat, sizei.w, sizei.h, 0, glFormat, glType, buffer);

        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);
        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR_MIPMAP_LINEAR);
        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);

        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAX_LEVEL, 0);

        return new Texture(textureId, sizei, internalFormat, glFormat, glType, buffer);
    }

    public OvrSizei getSize() {
        return size;
    }
}
