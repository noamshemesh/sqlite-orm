sqlite-orm
==========

Status: IN DEVELOPMENT (Not even close to stable)
-------------------------------------------------

Basic super lightweight "ORM" for android's sqlite.

Hibernate [or any other fully supported JPA ORM], in most of the times is too heavy for Android application, and for most applications it's not necessary because there are not enough beans.

And yet, we want a library that will simplify our access to SQLite, instead of working with the SQLiteDatabase object.

SQlite-ORM supports all the basic data types of Java, and will support more by demand.

Immediate TODO:
  1. findByProperties, update
  2. Automatic bean construct
  3. Support for projection (?)
  4. Documentation (in code and tutorial)
  5. Tests

Future TODO:
  1. Bean factory/generator
  2. OpenHelper wrapper
  3. Mavenize
  4. Sessions (?)