package com.appdynamics.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Database {

	public static final String APP_NAME = "Database";
	static final Logger LOG = LogManager.getLogger(Database.class.getName());

	private static DBs DATABASES =null;

	public static void main(String[] args) throws JAXBException, ClassNotFoundException {
		new Database();
	}

	public Database() throws JAXBException, ClassNotFoundException  {
		// Initialize sqlite schema

		JAXBContext jaxbContext = JAXBContext.newInstance(DBs.class);
		Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		DATABASES = (DBs) jaxbUnMarshaller.unmarshal(Database.class.getResourceAsStream("/dbs.xml"));
		initializeDB();
	}

	public static void log(Object... objects) {
		 LOG.info(APP_NAME + ": " +
		 Arrays.stream(objects).map(Object::toString).collect(Collectors.joining(" ")));
//		System.out.println(
//				APP_NAME + ": " + Arrays.stream(objects).map(Object::toString).collect(Collectors.joining(" ")));
	}

	public static String getDBPath(DBs dbs){
		return dbs.config.properties.stream().filter(prop -> {
			return prop.name.equals("path");
		}).findFirst().get().value;
	}
	
	private void initializeDB() throws ClassNotFoundException {
		DBs dbs = DATABASES;
		Class.forName("org.sqlite.JDBC");
		
		dbs.dbs.stream().forEach(db -> {
			log("Creating database", db.name);
			Connection connection =null;
			try {
				connection = DriverManager.getConnection("jdbc:sqlite:"+getDBPath(dbs)+"/" + db.name);
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(30); // set timeout to 30 sec.
				db.schema.tables.stream().forEach(table -> {
					log("Creating table", table.name);
					try {
						statement.executeUpdate(table.create);
						table.populator.inserts.stream().forEach(insertSql ->{
							try{
								log("Inserting", insertSql);
								statement.executeUpdate(insertSql);
							} catch (Exception e) {
								e.printStackTrace();
								throw new UnForcedException();
							}
						});
						
					} catch (Exception e) {
						e.printStackTrace();
						throw new UnForcedException();
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
				throw new UnForcedException();
			}finally{
				try {
					if(!connection.isClosed()){
						connection.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new UnForcedException();
				}
			}

		});
	}
}
