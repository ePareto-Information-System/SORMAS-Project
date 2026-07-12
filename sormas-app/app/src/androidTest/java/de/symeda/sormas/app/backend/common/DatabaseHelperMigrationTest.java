package de.symeda.sormas.app.backend.common;

import static org.junit.Assert.assertEquals;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperMigrationTest {

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
			assertEquals(0, countRows(db, "customizableEnumValue", "diseases LIKE '%AHF%'"));
			assertEquals(1, countRows(db, "customizableEnumValue", "diseases = 'EVD,UNSPECIFIED_VHF'"));
		} finally {
			db.close();
		}
	}

	private void createSchemaWithLegacyValues(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE diseaseConfiguration (id INTEGER PRIMARY KEY, disease TEXT)");
		db.execSQL("CREATE TABLE facility_diseaseConfiguration (facility_id INTEGER, diseaseConfiguration_id INTEGER)");
		db.execSQL("INSERT INTO diseaseConfiguration (id, disease) VALUES (1, 'AHF'), (2, 'UNSPECIFIED_VHF')");
		db.execSQL("INSERT INTO facility_diseaseConfiguration (facility_id, diseaseConfiguration_id) VALUES (10, 1), (10, 2), (20, 1)");

		for (String[] diseaseColumn : DISEASE_COLUMNS) {
			if (!tableExists(db, diseaseColumn[0])) {
				db.execSQL("CREATE TABLE " + diseaseColumn[0] + " (" + diseaseColumn[1] + " TEXT)");
			} else {
				db.execSQL("ALTER TABLE " + diseaseColumn[0] + " ADD COLUMN " + diseaseColumn[1] + " TEXT");
			}
		}
		for (String[] diseaseColumn : DISEASE_COLUMNS) {
			db.execSQL("INSERT INTO " + diseaseColumn[0] + " (" + diseaseColumn[1] + ") VALUES ('AHF')");
		}

		db.execSQL("CREATE TABLE customizableEnumValue (diseases TEXT)");
		db.execSQL("INSERT INTO customizableEnumValue (diseases) VALUES ('EVD,AHF')");
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
