package br.com.sdum.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MensagemProperties {

	private Properties properties = new Properties();

	private void init() throws IOException {

		this.properties = new Properties();

		String fileName = "mensagem.properties";

		InputStream inputStream = getClass().getClassLoader() .getResourceAsStream(fileName);

		if (inputStream != null) {
			this.properties.load(inputStream);
		} else {
			throw new FileNotFoundException("arquivo " + fileName+ " não encontrado!");
		}
	}

	public Properties getProperties() throws IOException {
		return this.properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public MensagemProperties(){
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
