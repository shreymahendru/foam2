package foam.nanos.script.JavaCompiler;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

// Fake file object that is made from the source code as string
class InMemoryJavaSourceFile extends SimpleJavaFileObject {

  private final String source_;

  protected InMemoryJavaSourceFile(String name, String source) {
    super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),
      Kind.SOURCE);

    this.source_ = source;
  }

  // instead or reading a file. return the source saved as a string
  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
    System.out.println("getting char content");
    return this.source_;
  }
}
