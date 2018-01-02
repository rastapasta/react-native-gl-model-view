package com.rnglmodelview;

import com.rnglmodelview.exceptions.IndexTypeNotSupportedException;
import com.rnglmodelview.exceptions.NormalTypeNotSupportedException;
import com.rnglmodelview.exceptions.PrimitiveTypeNotSupportedException;
import com.rnglmodelview.exceptions.UVTypeNotSupportedException;
import com.rnglmodelview.exceptions.VertexTypeNotSupportedException;
import com.threed.jpct.Object3D;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

public class RNGLModelViewModelLoader {
  /**
   * Loads a .model file
   * @param modelStream the InputStream of the .model file
   * @return the parsed .model file as an Object3D object
   * @throws IOException
   * @throws IndexTypeNotSupportedException
   * @throws PrimitiveTypeNotSupportedException
   * @throws VertexTypeNotSupportedException
   * @throws UVTypeNotSupportedException
   * @throws NormalTypeNotSupportedException
   */
  public static Object3D loadMODEL(InputStream modelStream) throws
      IOException,
      IndexTypeNotSupportedException,
      PrimitiveTypeNotSupportedException,
      VertexTypeNotSupportedException,
      UVTypeNotSupportedException,
      NormalTypeNotSupportedException {
    // Note: The .model format byte order is in little endian
    byte[] fileIdentifier = new byte[32];

    // We don't do anything with the version yet. If some .model files don't work in the future,
    // we might need to do a version check
    byte[] majorVersionBytes = new byte[4];
    byte[] minorVersionBytes = new byte[4];

    // Read the header
    modelStream.read(fileIdentifier, 0, 32);
    modelStream.read(majorVersionBytes, 0, 4);
    modelStream.read(minorVersionBytes, 0, 4);

    // Read the table of contents
    byte[] attribHeaderSizeBytes = new byte[4];
    byte[] indexBufferOffsetBytes = new byte[4];
    byte[] vertexBufferOffsetBytes = new byte[4];
    byte[] uvBufferOffsetBytes = new byte[4];
    byte[] normalBufferOffsetBytes = new byte[4];

    modelStream.read(attribHeaderSizeBytes, 0, 4);
    modelStream.read(indexBufferOffsetBytes, 0, 4);
    modelStream.read(vertexBufferOffsetBytes, 0, 4);
    modelStream.read(uvBufferOffsetBytes, 0, 4);
    modelStream.read(normalBufferOffsetBytes, 0, 4);

    int[] indices = getModelIndices(modelStream);
    float[] vertexElements = getModelVertexElements(modelStream);
    float[] uvElements = getModelUVElements(modelStream);
    float[] normalElements = getModelNormalElements(modelStream);

    return new Object3D(vertexElements, normalElements, uvElements, indices, 0);
  }

  private static int[] getModelIndices(InputStream modelStream)
      throws IOException, IndexTypeNotSupportedException, PrimitiveTypeNotSupportedException {
    byte[] indexBufferSize32Bits = new byte[4];
    byte[] dataTypeBytes = new byte[4];
    byte[] primTypeBytes = new byte[4];
    byte[] sizePerElementBytes = new byte[4];
    byte[] numIndices32Bits = new byte[4];

    modelStream.read(indexBufferSize32Bits, 0, 4);
    modelStream.read(dataTypeBytes, 0, 4);
    modelStream.read(primTypeBytes, 0, 4);
    modelStream.read(sizePerElementBytes, 0, 4);
    modelStream.read(numIndices32Bits, 0, 4);

    int indexBufferSize = ByteBuffer.wrap(indexBufferSize32Bits).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int primType = ByteBuffer.wrap(primTypeBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int numIndices = ByteBuffer.wrap(numIndices32Bits).order(ByteOrder.LITTLE_ENDIAN).getInt();

    if (primType != GL10.GL_TRIANGLES) {
      // We only support the triangle primitives right now
      throw new PrimitiveTypeNotSupportedException("Only triangle primitives are supported");
    }

    int indexTypeSize = indexBufferSize / numIndices;

    int[] indices = new int[numIndices];
    byte[] currentIndexBytes = new byte[indexTypeSize];

    // Read the indices into a buffer
    for (int i = 0; i < numIndices; i++) {
      modelStream.read(currentIndexBytes, 0, indexTypeSize);

      int currentIndex = -1;

      switch (indexTypeSize) {
        case 4:
          currentIndex = ByteBuffer.wrap(currentIndexBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
          break;
        case 2:
          currentIndex = ByteBuffer.wrap(currentIndexBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
          break;
        case 1:
          currentIndex = currentIndexBytes[0];
          break;
        default:
          // We don't support other types
          throw new IndexTypeNotSupportedException("Only integer, short and byte indices are supported");
      }

      indices[i] = currentIndex;
    }

    return indices;
  }

  private static float[] getModelVertexElements(InputStream modelStream)
      throws IOException, VertexTypeNotSupportedException {
    byte[] vertexELementBufferSize32Bits = new byte[4];
    byte[] dataTypeBytes = new byte[4];
    byte[] primTypeBytes = new byte[4];
    byte[] sizePerElementBytes = new byte[4];
    byte[] numVertexElements32Bits = new byte[4];

    modelStream.read(vertexELementBufferSize32Bits, 0, 4);
    modelStream.read(dataTypeBytes, 0, 4);
    modelStream.read(primTypeBytes, 0, 4);
    modelStream.read(sizePerElementBytes, 0, 4);
    modelStream.read(numVertexElements32Bits, 0, 4);

    int vertexElementBufferSize = ByteBuffer.wrap(vertexELementBufferSize32Bits).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int dataType = ByteBuffer.wrap(dataTypeBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int sizePerElement = ByteBuffer.wrap(sizePerElementBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int numVertices = ByteBuffer.wrap(numVertexElements32Bits).order(ByteOrder.LITTLE_ENDIAN).getInt();

    if (dataType != GL10.GL_FLOAT) {
      throw new VertexTypeNotSupportedException("Only float vertices are supported");
    }

    // We have multiple elements per vertex and we will eventually throw away the fourth elements (w)
    int numVertexElements = numVertices * (sizePerElement == 4 ? 3 : sizePerElement);
    int vertexElementSize = vertexElementBufferSize / numVertices / sizePerElement;
    float[] vertexElements = new float[numVertexElements];
    byte[] currentVertexElement32Bit = new byte[vertexElementSize];

    // Read the indices into a buffer
    for (int i = 0; i < numVertexElements; i++) {
      modelStream.read(currentVertexElement32Bit, 0, vertexElementSize);
      float currentVertexElement = ByteBuffer.wrap(currentVertexElement32Bit).order(ByteOrder.LITTLE_ENDIAN).getFloat();
      vertexElements[i] = currentVertexElement;

      // If the model contains Vector4s, we need to throw away the fourth element (w) here
      if (sizePerElement == 4 && (i + 1) % 3 == 0) {
        modelStream.read(currentVertexElement32Bit, 0, vertexElementSize);
      }
    }

    return vertexElements;
  }

  private static float[] getModelUVElements(InputStream modelStream)
      throws IOException, UVTypeNotSupportedException {
    byte[] uvBufferSize32Bits = new byte[4];
    byte[] dataTypeBytes = new byte[4];
    byte[] primTypeBytes = new byte[4];
    byte[] sizePerElementBytes = new byte[4];
    byte[] numUVs32Bits = new byte[4];

    modelStream.read(uvBufferSize32Bits, 0, 4);
    modelStream.read(dataTypeBytes, 0, 4);
    modelStream.read(primTypeBytes, 0, 4);
    modelStream.read(sizePerElementBytes, 0, 4);
    modelStream.read(numUVs32Bits, 0, 4);

    int uvBufferSize = ByteBuffer.wrap(uvBufferSize32Bits).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int dataType = ByteBuffer.wrap(dataTypeBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int sizePerElement = ByteBuffer.wrap(sizePerElementBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int numUVs = ByteBuffer.wrap(numUVs32Bits).order(ByteOrder.LITTLE_ENDIAN).getInt();

    if (dataType != GL10.GL_FLOAT) {
      throw new UVTypeNotSupportedException("Only float UVs are supported");
    }

    // We have multiple elements per uv
    int numUVElements = numUVs * sizePerElement;
    int uvElementTypeSize = uvBufferSize / numUVElements;
    float[] uvElements = new float[numUVElements];
    byte[] currentUVElement32Bit = new byte[uvElementTypeSize];

    // Read the indices into a buffer
    for (int i = 0; i < numUVElements; i++) {
      modelStream.read(currentUVElement32Bit, 0, uvElementTypeSize);
      float currentUVElement = ByteBuffer.wrap(currentUVElement32Bit).order(ByteOrder.LITTLE_ENDIAN).getFloat();
      uvElements[i] = currentUVElement;
    }

    return uvElements;
  }

  private static float[] getModelNormalElements(InputStream modelStream)
      throws IOException, NormalTypeNotSupportedException {
    byte[] normalElementBufferSize32Bit = new byte[4];
    byte[] dataTypeBytes = new byte[4];
    byte[] primTypeBytes = new byte[4];
    byte[] sizePerElementBytes = new byte[4];
    byte[] numNormalElements32Bit = new byte[4];

    modelStream.read(normalElementBufferSize32Bit, 0, 4);
    modelStream.read(dataTypeBytes, 0, 4);
    modelStream.read(primTypeBytes, 0, 4);
    modelStream.read(sizePerElementBytes, 0, 4);
    modelStream.read(numNormalElements32Bit, 0, 4);

    int normalElementBufferSize = ByteBuffer.wrap(normalElementBufferSize32Bit).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int dataType = ByteBuffer.wrap(dataTypeBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int sizePerElement = ByteBuffer.wrap(sizePerElementBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    int numNormals = ByteBuffer.wrap(numNormalElements32Bit).order(ByteOrder.LITTLE_ENDIAN).getInt();

    if (dataType != GL10.GL_FLOAT) {
      throw new NormalTypeNotSupportedException("Only float normals are supported");
    }

    // We have multiple elements per normal
    int numNormalElements = numNormals * sizePerElement;
    int normalElementSize = normalElementBufferSize / numNormalElements;
    float[] normalElements = new float[numNormalElements];
    byte[] currentNormalElement32Bit = new byte[normalElementSize];

    // Read the indices into a buffer
    for (int i = 0; i < numNormalElements; i++) {
      modelStream.read(currentNormalElement32Bit, 0, normalElementSize);
      float currentNormalElement = ByteBuffer.wrap(currentNormalElement32Bit).order(ByteOrder.LITTLE_ENDIAN).getFloat();
      normalElements[i] = currentNormalElement;
    }

    return normalElements;
  }
}

