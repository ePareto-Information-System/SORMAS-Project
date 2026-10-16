package de.symeda.sormas.app.backend.common;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperMigrationTest {

	// Every persisted Android field backed by the Disease enum. Criteria objects
	// are intentionally excluded because they are not stored in SQLite.
	private static final String[][] DISEASE_COLUMNS = {
		{ "cases", "disease" },
		{ "clinicalVisit", "disease" },
		{ "contacts", "caseDisease" },
		{ "diseaseClassificationCriteria", "disease" },
		{ "forms", "disease" },
		{ "events", "disease" },
		{ "visits", "disease" },
		{ "featureConfiguration", "disease" },
		{ "immunization", "disease" },
		{ "aggregateReport", "disease" },
		{ "weeklyreportentry", "disease" },
		{ "outbreak", "disease" },
		{ "pathogenTest", "testedDisease" },
		{ "pathogenTest", "secondTestedDisease" },
		{ "person", "causeOfDeathDisease" },
		{ "samples", "suspectedDisease" },
		{ "users", "limitedDisease" }
	};

	@Test
	public void shouldMigrateLegacyAhfValuesAndMergeDiseaseConfigurations() {
		SQLiteDatabase db = SQLiteDatabase.create(null);
		try {
			createSchemaWithLegacyValues(db);

			DatabaseHelper.migrateLegacyAhfDisease(db);
			DatabaseHelper.migrateLegacyAhfDisease(db);

			for (String[] diseaseColumn : DISEASE_COLUMNS) {
				assertEquals(0, count(db, diseaseColumn[0], diseaseColumn[1], "AHF"));
				assertEquals(1, count(db, diseaseColumn[0], diseaseColumn[1], "UNSPECIFIED_VHF"));
			}
			assertEquals(0, count(db, "diseaseConfiguration", "disease", "AHF"));
			assertEquals(1, count(db, "diseaseConfiguration", "disease", "UNSPECIFIED_VHF"));
			assertEquals(1, countRows(db, "facility_diseaseConfiguration", "facility_id = 10 AND diseaseConfiguration_id = 2"));
			assertEquals(1, countRows(db, "facility_diseaseConfiguration", "facility_id = 20 AND diseaseConfiguration_id = 2"));
			assertEquals(0, countRows(db, "customizableEnumValue", "',' || diseases || ',' LIKE '%,AHF,%'"));
			// the migration must force a re-pull of disease configurations
			assertEquals(0, countRows(db, "diseaseConfiguration", "changeDate <> 0"));
			assertEquals(1, countRows(db, "customizableEnumValue", "diseases = 'EVD,UNSPECIFIED_VHF'"));
			assertEquals(1, countRows(db, "customizableEnumValue", "diseases = 'EVD,AHFISH'"));
			assertEquals(1, countRows(db, "forms", "changeDate = 123 AND localChangeDate = 456 AND modified = 1"));
		} finally {
			db.close();
		}
	}

	@Test
	public void shouldRepairLegacyAhfValuesFromVersion410() {
		Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
		String databaseName = "ahf-migration-" + System.nanoTime() + ".db";
		context.deleteDatabase(databaseName);
		DatabaseHelper helper = new DatabaseHelper(context, databaseName);
		SQLiteDatabase db = SQLiteDatabase.create(null);
		try {
			createSchemaWithLegacyValues(db);
			db.setVersion(410);

			helper.onUpgrade(db, null, 410, DatabaseHelper.DATABASE_VERSION);
			// SQLiteOpenHelper sets the user version after onUpgrade returns successfully.
			db.setVersion(DatabaseHelper.DATABASE_VERSION);

			assertEquals(DatabaseHelper.DATABASE_VERSION, db.getVersion());
			for (String[] diseaseColumn : DISEASE_COLUMNS) {
				assertEquals(0, count(db, diseaseColumn[0], diseaseColumn[1], "AHF"));
			}
			assertEquals(0, count(db, "diseaseConfiguration", "disease", "AHF"));
			assertEquals(1, count(db, "diseaseConfiguration", "disease", "UNSPECIFIED_VHF"));
			assertEquals(1, countRows(db, "forms", "changeDate = 123 AND localChangeDate = 456 AND modified = 1"));
		} finally {
			db.close();
			helper.close();
			context.deleteDatabase(databaseName);
		}
	}

	@Test
	public void shouldRenameLegacyDiseaseConfigurationWhenReplacementIsMissing() {
		SQLiteDatabase db = SQLiteDatabase.create(null);
		try {
			createSchemaWithLegacyValues(db, false);

			DatabaseHelper.migrateLegacyAhfDisease(db);

			assertEquals(0, count(db, "diseaseConfiguration", "disease", "AHF"));
			assertEquals(1, countRows(db, "diseaseConfiguration", "id = 1 AND disease = 'UNSPECIFIED_VHF'"));
			assertEquals(1, countRows(db, "facility_diseaseConfiguration", "facility_id = 10 AND diseaseConfiguration_id = 1"));
			assertEquals(1, countRows(db, "facility_diseaseConfiguration", "facility_id = 20 AND diseaseConfiguration_id = 1"));
			assertEquals(0, countRows(db, "diseaseConfiguration", "changeDate <> 0"));
		} finally {
			db.close();
		}
	}

	private void createSchemaWithLegacyValues(SQLiteDatabase db) {
		createSchemaWithLegacyValues(db, true);
	}

	private void createSchemaWithLegacyValues(SQLiteDatabase db, boolean includeReplacementConfiguration) {
		db.execSQL("CREATE TABLE diseaseConfiguration (id INTEGER PRIMARY KEY, disease TEXT, changeDate INTEGER)");
		db.execSQL("CREATE TABLE facility_diseaseConfiguration (facility_id INTEGER, diseaseConfiguration_id INTEGER)");
		db.execSQL("INSERT INTO diseaseConfiguration (id, disease, changeDate) VALUES (1, 'AHF', 111)");
		if (includeReplacementConfiguration) {
			db.execSQL("INSERT INTO diseaseConfiguration (id, disease, changeDate) VALUES (2, 'UNSPECIFIED_VHF', 222)");
			db.execSQL("INSERT INTO facility_diseaseConfiguration (facility_id, diseaseConfiguration_id) VALUES (10, 1), (10, 2), (20, 1)");
		} else {
			db.execSQL("INSERT INTO facility_diseaseConfiguration (facility_id, diseaseConfiguration_id) VALUES (10, 1), (20, 1)");
		}

		for (String[] diseaseColumn : DISEASE_COLUMNS) {
			if (!tableExists(db, diseaseColumn[0])) {
				if ("forms".equals(diseaseColumn[0])) {
					db.execSQL("CREATE TABLE forms (disease TEXT, changeDate INTEGER, localChangeDate INTEGER, modified INTEGER)");
				} else {
					db.execSQL("CREATE TABLE " + diseaseColumn[0] + " (" + diseaseColumn[1] + " TEXT)");
				}
			} else {
				db.execSQL("ALTER TABLE " + diseaseColumn[0] + " ADD COLUMN " + diseaseColumn[1] + " TEXT");
			}
		}
		for (String[] diseaseColumn : DISEASE_COLUMNS) {
			if ("forms".equals(diseaseColumn[0])) {
				db.execSQL("INSERT INTO forms (disease, changeDate, localChangeDate, modified) VALUES ('AHF', 123, 456, 1)");
			} else {
				db.execSQL("INSERT INTO " + diseaseColumn[0] + " (" + diseaseColumn[1] + ") VALUES ('AHF')");
			}
		}

		db.execSQL("CREATE TABLE customizableEnumValue (diseases TEXT)");
		db.execSQL("INSERT INTO customizableEnumValue (diseases) VALUES ('EVD,AHF')");
		db.execSQL("INSERT INTO customizableEnumValue (diseases) VALUES ('EVD,AHFISH')");
	}

	private boolean tableExists(SQLiteDatabase db, String tableName) {
		return countRows(db, "sqlite_master", "type = 'table' AND name = '" + tableName + "'") > 0;
	}

	private int count(SQLiteDatabase db, String tableName, String columnName, String value) {
		return countRows(db, tableName, columnName + " = '" + value + "'");
	}

	private int countRows(SQLiteDatabase db, String tableName, String whereClause) {
		try (Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName + " WHERE " + whereClause, null)) {
			cursor.moveToFirst();
			return cursor.getInt(0);
		}
	}
}
