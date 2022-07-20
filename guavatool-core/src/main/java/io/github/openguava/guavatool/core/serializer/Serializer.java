package io.github.openguava.guavatool.core.serializer;

import io.github.openguava.guavatool.core.exception.SerializationException;

public interface Serializer<T, V> {

	V serialize(T t) throws SerializationException;
	
	T deserialize(V value) throws SerializationException;
}
