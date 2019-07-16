# nl-dgl-romix-akka-kryo-serialization
Adaptation of Romix's akka-kryo-serialization for use with tinkerpop's shaded kryo 

see https://github.com/romix/scala-kryo-serialization

Tinkerpop shades Kryo and Romix developed great scala serializers for kryo. 
Due to the shading its not possible to use Romix's serializers because they use the original com.esotericsoftware.kryo packages. 
This adaptation simply rewrites all the imports from com.esotericsoftware.kryo to org.apache.tinkerpop.shaded.kryo (and a bit more).
