package org.app.client.updater.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

//import eu.vahlas.json.schema.JSONSchema;
//import eu.vahlas.json.schema.JSONSchemaProvider;
//import eu.vahlas.json.schema.impl.JacksonSchemaProvider;

public final class JSonUtil {

	private static Logger LOG = Logger.getLogger(JSonUtil.class);

	public static String EXTENSION = ".json";

	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static String CR_SYMBOL = "#";

	public static void writeJson(final String file, final Object obj, final Class<?> clazz) throws IOException {
		writeJsonStream(new FileOutputStream(new File(file)), obj, clazz);
	}

	public static void writeJson(final File file, final Object obj, final Class<?> clazz) throws IOException {
		writeJsonStream(new FileOutputStream(file), obj, clazz);
	}

	private static void writeJsonStream(final OutputStream out, final Object obj, final Class<?> clazz) throws IOException {
		JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
		writer.setIndent("  ");
		new Gson().toJson(obj, clazz, writer);
		writer.close();
	}

	public static Object readJson(final String file, final Class<?> clazz) throws IOException {
		return readJson(new File(file), clazz);
	}

	public static Object readJson(final File file, final Class<?> clazz) throws IOException {
		return readJsonStream(new FileInputStream(file), clazz);
	}

	public static InputStreamReader readJsonStream(final File file) throws IOException {
		return new InputStreamReader(new FileInputStream(file), "UTF-8");
	}

	public static Object readJsonStream(final InputStream in, final Class<?> clazz) throws IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		Object obj = new Gson().fromJson(reader, clazz);
		reader.close();
		return obj;
	}

	public static String toJSon(final Object content, final Class<?> clazz) {
		return gson.toJson(content, clazz);
	}

	public static String toFlatJSon(Object content, Class<?> clazz) {
		return new Gson().toJson(content, clazz);
	}

	public static Object fromJSon(String content, Class<?> clazz) {
		return new Gson().fromJson(content, clazz);
	}

	public static Object readRemoteJson(String remotePath, Class<?> clazz) throws IOException, InvalidFormatException {
		LOG.debug("Reading remote file: " + remotePath);
		String remoteContent = HttpUtil.getRemoteFileContent(remotePath);
		LOG.debug("Validate json file from schema: " + clazz.getSimpleName());
		boolean valid = validateSchema(remoteContent, retrieveSchema(clazz));
		if (!valid) {
			throw new InvalidClassException("Invalid json file format: " + remotePath);
		}
		LOG.debug("Mapping file to json object: " + remotePath);
		return fromJSon(remoteContent, clazz);
	}

	public static boolean writeRemoteJson(final String localFilePath, final String remotePath) throws IOException {
		return HttpUtil.putRemoteJson(localFilePath, remotePath);
	}

	public static JsonSchema generateSchema(final String jsonFile, final Class<?> clazz) throws JsonMappingException {
		return generateSchema(jsonFile, clazz, new ObjectMapper());
	}

	public static JsonSchema generateSchema(final String jsonFile, final Class<?> clazz, ObjectMapper mapper) throws JsonMappingException {
		SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
		mapper.acceptJsonFormatVisitor(mapper.constructType(clazz), visitor);
		return visitor.finalSchema();
	}

	public static void createSchema(final String jsonFile, final Class<?> clazz) throws JsonMappingException, JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonSchema jsonSchema = generateSchema(jsonFile, clazz, mapper);
		String jsonSchemaString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema);
		Files.write(jsonSchemaString, new File(jsonFile), Charsets.UTF_8);
	}

	public static InputStream retrieveSchema(final Class<?> schemaClazz) throws IOException {
		return ResourceUtil.loadResource(schemaClazz.getSimpleName() + JSonUtil.EXTENSION);
	}

	public static boolean validateSchema(final Object jsonInstance, final InputStream appVersionSchema) throws JsonMappingException,
			JsonProcessingException, IOException {
		List<String> errors = null;
		org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
		JSONSchemaProvider schemaProvider = new JacksonSchemaProvider(mapper);
		JSONSchema schema = schemaProvider.getSchema(appVersionSchema);
		if (jsonInstance instanceof String) {
			errors = schema.validate((String) jsonInstance);
		} else if (jsonInstance instanceof InputStream) {
			errors = schema.validate((InputStream) jsonInstance);
		} else if (jsonInstance instanceof URL) {
			errors = schema.validate((URL) jsonInstance);
		} else if (jsonInstance instanceof Reader) {
			errors = schema.validate((Reader) jsonInstance);
		}
		if (errors != null && !errors.isEmpty()) {
			LOG.error("Validate json content against schema");
			for (String s : errors) {
				LOG.error(s);
			}
		}

		if (errors == null || !errors.isEmpty()) {
			return false;
		}
		return true;
	}

}
