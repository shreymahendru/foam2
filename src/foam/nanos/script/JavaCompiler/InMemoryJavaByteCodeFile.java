package foam.nanos.script.JavaCompiler;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

class InMemoryJavaByteCodeFile extends SimpleJavaFileObject {

  private ByteArrayOutputStream _outputStream;


  protected InMemoryJavaByteCodeFile(String name) {
    super(URI.create("bytes:///" + name + name.replace('.', '/')), Kind.CLASS);

    this._outputStream = new ByteArrayOutputStream();
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    return this._outputStream;
  }

  public byte[] getBytes() {
    return this._outputStream.toByteArray();
  }
}
