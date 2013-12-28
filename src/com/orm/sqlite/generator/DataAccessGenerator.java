package com.orm.sqlite.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.google.common.base.CaseFormat;

public class DataAccessGenerator {
	private static final String TEMPLATE_PATH = "res/template/dataaccess.vm";

	private static List<String> getTypes(Field[] fields, boolean dbType) {
		List<String> toReturn = new ArrayList<String>();
		for (Field field : fields) {
			if (!field.getName().toLowerCase().equals("id")) {
				toReturn.add(dbType ? getSqliteType(field.getType()) : getExplicitType(field.getType()));
			}
		}
		return toReturn;
	}

	private static List<String> getNames(Field[] fields, boolean underscore) {
		List<String> toReturn = new ArrayList<String>();
		for (Field field : fields) {
			if (!field.getName().toLowerCase().equals("id")) {
				toReturn.add(underscore ? CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()) : field
						.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1));
			}
		}
		return toReturn;
	}

	private static String getExplicitType(Class<?> type) {
		if (type == byte[].class) {
			return "ByteArray";
		} else {
			return type.getSimpleName();
		}
	}

	private static String getSqliteType(Class<?> type) {
		if (type == String.class) {
			return "text";
		} else if (type == byte[].class) {
			return "blob";
		} else {
			return "number";
		}
	}

	public static void main(String[] args) {
		try {
			Velocity.init();

			VelocityContext context = new VelocityContext();

			Class<?> beanClazz = Class.forName(args[0]);
			Field[] fields = beanClazz.getDeclaredFields();
			context.put("allColumnsDb", getNames(fields, true));
			context.put("allColumnsCamel", getNames(fields, false));
			context.put("allTypesDb", getTypes(fields, true));
			context.put("allTypesExplicit", getTypes(fields, false));
			context.put("beanName", beanClazz.getSimpleName());

			Template template = null;

			template = Velocity.getTemplate(TEMPLATE_PATH);

			StringWriter sw = new StringWriter();

			template.merge(context, sw);

			System.out.println(sw.toString());
			File genFolder = new File("./gen");
			genFolder.mkdir();
			File dataAccessFile = new File("gen" + File.separator + beanClazz.getSimpleName() + "DataAccess.java");
			if (dataAccessFile.exists()) {
				dataAccessFile.delete();
			}
			FileWriter fw = new FileWriter(dataAccessFile);
			fw.write(sw.toString());
			fw.close();
			sw.close();
		} catch (ResourceNotFoundException e) {
			System.out.println("Couldn't the template " + TEMPLATE_PATH + " " + e.toString());
		} catch (ParseErrorException e) {
			System.out.println("syntax error: problem parsing the template " + e.toString());
		} catch (MethodInvocationException e) {
			System.out.println("something invoked in the template " + e.toString());
		} catch (Exception e) {
			System.out.println("Unrecognized exception " + e.getMessage());
			e.printStackTrace();
		}

	}

}
