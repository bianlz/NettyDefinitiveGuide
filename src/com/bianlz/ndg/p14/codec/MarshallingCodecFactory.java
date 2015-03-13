package com.bianlz.ndg.p14.codec;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;



public final class MarshallingCodecFactory {
	protected static Marshaller buildMarshalling()throws IOException{
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration config = new MarshallingConfiguration();
		config.setVersion(5);
		Marshaller marshaller = marshallerFactory.createMarshaller(config);
		return marshaller;
		
	}
	protected static Unmarshaller buildUnMarshalling()throws IOException{
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration config = new MarshallingConfiguration();
		config.setVersion(5);
		Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(config);
		return unmarshaller;
	}
}
