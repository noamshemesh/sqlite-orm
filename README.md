sqlite-orm
==========

Status: BETA
------------

Basic super lightweight "ORM" for android's sqlite.

Hibernate [or any other fully supported JPA ORM], in most of the times is too heavy for Android application, and for most applications it's not necessary because there are not enough beans.

And yet, we want a library that will simplify our access to SQLite, instead of working with the SQLiteDatabase object.

SQlite-ORM supports all the basic data types of Java, and will support more by demand.

How to Use
----------

Download the jar that distributed under dist folder and make your project depend in it.
Create java beans with simple types, follow this rules:
1. ID column must be first
2. Make sure the constructor parameters are in the same order as the fields, including the ID column
Now you need to create a DataAccess class, the generator can help with that, run it from your project folder by using this command:
```
java -cp "[your compiled classes]:[downloaded jar]:[sqlite-orm lib folder]/*"
```
com.orm.sqlite.generator.DataAccessGenerator [your class full name]
for example:
```
java -cp "dist/sqlite-orm-0.4.jar:lib/*" com.orm.sqlite.generator.DataAccessGenerator com.orm.sqlite.example.Example
```
You can also use your favorite IDE in order to run the generator.

The output will be in folder named "gen" under your project, copy it to the package of all the DAO's

All the DAO's have the same basic interface as BaseDataAccess that documented [here][0]

TODO:
  1. Bean factory/generator
  2. OpenHelper wrapper
  3. Sessions (?)
  4. Release in Maven central repository


[0]: http://noamshemesh.github.io/sqlite-orm

