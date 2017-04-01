import os
basedir = os.path.abspath( os.path.dirname( __file__ ) )

SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join( basedir, "app.db" )
SQLALCHEMY_MIGRATE_REPO = os.path.join( basedir, "db_repository" )

FILE_UPLOAD_FOLDER = os.path.join( basedir, "data" )
IMAGE_UPLOAD_FOLDER = os.path.join( FILE_UPLOAD_FOLDER, "data/progvar_raw" )
CLASSIFIER_UPLOAD_FOLDER = os.path.join( FILE_UPLOAD_FOLDER, "classifiers" )
CLASSROOM_UPLOAD_FOLDER = os.path.join( FILE_UPLOAD_FOLDER, "classroom" )