package foam.nanos.script.JavaCompiler;


import java.util.Map;

class MapClassLoader extends ClassLoader
{
  private Map<String, InMemoryJavaByteCodeFile> _compiledClassData;

  public MapClassLoader(Map<String, InMemoryJavaByteCodeFile> map)
  {
    super();
    this._compiledClassData = map;
  }

  @Override
  public Class<?> findClass(String name)
  {
    InMemoryJavaByteCodeFile byteCodeFile = _compiledClassData.get(name);
    byte[] byteCodeArray = byteCodeFile.getBytes();
    return defineClass(name, byteCodeArray, 0, byteCodeArray.length);
  }
}

